package com.advanced.protection.systems.multisensor.rfdata.service.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.advanced.protection.systems.multisensor.modelservice.constant.DataType;
import com.advanced.protection.systems.multisensor.modelservice.constant.ErrorType;
import com.advanced.protection.systems.multisensor.modelservice.constant.SensorType;
import com.advanced.protection.systems.multisensor.modelservice.dto.DataDto;
import com.advanced.protection.systems.multisensor.modelservice.param.DataParam;
import com.advanced.protection.systems.multisensor.rfdata.core.converter.RfDataConverter;
import com.advanced.protection.systems.multisensor.rfdata.core.util.KafkaMapperUtil;
import com.advanced.protection.systems.multisensor.rfdata.dao.RfDataRepository;
import com.advanced.protection.systems.multisensor.rfdata.domain.RfDataDocument;
import com.advanced.protection.systems.multisensor.rfdata.feignclient.FeignPositionClient;
import com.advanced.protection.systems.multisensor.rfdata.service.data.exception.RfDataException;
import com.advanced.protection.systems.multisensor.rfdata.service.error.ErrorServiceFacade;
import com.advanced.protection.systems.multisensor.rfdata.service.filer.RfDataFilter;

import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;

@Slf4j
@Component
public class RfDataService {

	private final RfDataRepository rfDataRepository;
	private final RfDataConverter rfDataConverter;
	private final KafkaReceiver<String, String> kafkaReceiver;
	private final FeignPositionClient feignPositionClient;
	private final ErrorServiceFacade errorServiceFacade;

	RfDataService(final RfDataRepository rfDataRepository,
				  final RfDataConverter rfDataConverter,
				  final KafkaReceiver<String, String> kafkaReceiver,
				  final FeignPositionClient feignPositionClient,
				  final ErrorServiceFacade errorServiceFacade) {
		this.rfDataRepository = rfDataRepository;
		this.rfDataConverter = rfDataConverter;
		this.kafkaReceiver = kafkaReceiver;
		this.feignPositionClient = feignPositionClient;
		this.errorServiceFacade = errorServiceFacade;
	}

	Flux<DataDto> getRfData(DataParam dataParam) {
		log.info("get rfDatas by dataParam: {} ", dataParam);
		return Try.of(() -> dataParam)
				.map(localDateTimes ->
						rfDataRepository.findByDataParam(dataParam.getRssi(), dataParam.getTimeAddedFrom(), dataParam.getTimeAddedTo(), dataParam.getSensorName())
				)
				.map(rfDataDocumentFlux -> rfDataDocumentFlux.collectList()
						.filter(rfDataDocuments -> !rfDataDocuments.isEmpty())
						.map(this::getRfDataDtos)
						.flatMapMany(Flux::fromIterable))
				.getOrNull();
	}

	Flux<DataDto> consumeRfData() {
		CountDownLatch latch = new CountDownLatch(20);
		return kafkaReceiver.receive()
				.checkpoint("RfData being consumed")
				.flatMap(receiverRecord -> saveRfData(latch, receiverRecord));
	}

	private Flux<DataDto> saveRfData(CountDownLatch latch,
									 ReceiverRecord<String, String> receiverRecord) {
		var offset = receiverRecord.receiverOffset();
		var dateFormat = new SimpleDateFormat("HH:mm:ss:SSS z dd MMM yyyy");
		log.info("Received message: topic-partition: {},  offset: {}, timestamp: {}, receivedKey; {} and receivedValue: {}",
				offset.topicPartition(),
				offset.offset(),
				dateFormat.format(new Date(receiverRecord.timestamp())),
				receiverRecord.key(),
				receiverRecord.value());
		offset.acknowledge();
		latch.countDown();
		return saveBySensorName(KafkaMapperUtil.fromBinary(receiverRecord.value(), DataDto.class))
				.flatMapMany(Flux::just);
	}

	private Mono<DataDto> saveBySensorName(DataDto dataDto) {
		log.info("save rfData by sensor unique name: {}", dataDto.getSensorDto().getName());
		var sensorName = dataDto.getSensorDto().getName();

		return Mono.just(dataDto)
				.filter(Objects::nonNull)
				.map(rfData -> getRfDataBySensorName(sensorName))
				.flatMap(rfDataDocumentMono -> rfDataDocumentMono
						.filter(Objects::isNull)
						.flatMap(data -> feignPositionClient.downloadBySensorName(sensorName))
						.filter(Objects::isNull)
						.flatMap(rfData -> saveBySensorType(dataDto))
						.switchIfEmpty(Mono.defer(() -> getError(ErrorType.SENSOR_UNIQUE_NAME_ERROR))));
	}

	private Mono<DataDto> saveBySensorType(DataDto dataDto) {
		log.info("save rfData by sensor type: {}", dataDto.getSensorDto().getSensorType());
		return Mono.just(dataDto)
				.filter(Objects::nonNull)
				.filter(rfData -> RfDataFilter.isSensorByType(dataDto, SensorType.RF))
				.map(rfDataConverter::dataDtoToRfDataDocument)
				.flatMap(rfDataRepository::save)
				.map(rfDataConverter::rfDataDocumentToDataDto)
				.map(this::setDataType)
				.switchIfEmpty(Mono.defer(() -> getError(ErrorType.SENSOR_TYPE_RF_DATA_ERROR)));
	}

	private Mono<DataDto> getError(ErrorType sensorTypeRfDataError) {
		errorServiceFacade.saveErrorDto(sensorTypeRfDataError)
				.subscribe();
		return Mono.error(new RfDataException(sensorTypeRfDataError));
	}

	Mono<DataDto> getRfDataBySensorName(String name) {
		log.info("get rfDataDocument by sensor name: {}", name);
		return rfDataRepository.findBySensorName(name)
				.filter(Objects::nonNull)
				.map(rfDataConverter::rfDataDocumentToDataDto);
	}

	private DataDto setDataType(DataDto dataDto) {
		dataDto.setDataType(DataType.RF);
		return dataDto;
	}

	private List<DataDto> getRfDataDtos(List<RfDataDocument> rfDataDocuments) {
		log.info("convert rfDataDocuments: {} to rfDataDto", rfDataDocuments);
		return rfDataDocuments.stream()
				.map(rfDataConverter::rfDataDocumentToDataDto)
				.collect(Collectors.toList());
	}

}
