package com.advanced.protection.systems.multisensor.rfdata.service.data;

import static com.advanced.protection.systems.multisensor.rfdata.core.util.RfDataUtil.CLIENT_IP;
import static com.advanced.protection.systems.multisensor.rfdata.core.util.RfDataUtil.RF_SENSOR_NAME;
import static com.advanced.protection.systems.multisensor.rfdata.core.util.RfDataUtil.createDataDto;
import static com.advanced.protection.systems.multisensor.rfdata.core.util.RfDataUtil.createRfData;
import static com.advanced.protection.systems.multisensor.rfdata.core.util.RfDataUtil.getDataParam;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.advanced.protection.systems.multisensor.modelservice.dto.DataDto;
import com.advanced.protection.systems.multisensor.rfdata.core.converter.RfDataConverter;
import com.advanced.protection.systems.multisensor.rfdata.core.util.DateUtil;
import com.advanced.protection.systems.multisensor.rfdata.dao.RfDataRepository;
import com.advanced.protection.systems.multisensor.rfdata.feignclient.FeignPositionClient;
import com.advanced.protection.systems.multisensor.rfdata.service.error.ErrorServiceFacade;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RfDataServiceTest {

	private static final String SENSOR_NAME = RF_SENSOR_NAME.concat("1");
	private final RfDataRepository rfDataRepository = mock(RfDataRepository.class);
	private final RfDataConverter rfDataConverter = mock(RfDataConverter.class);
	private final KafkaReceiver kafkaReceiver = mock(KafkaReceiver.class);
	private final FeignPositionClient feignPositionClient = mock(FeignPositionClient.class);
	private final ErrorServiceFacade errorServiceFacade = mock(ErrorServiceFacade.class);

	private RfDataService rfDataService = new RfDataService(rfDataRepository, rfDataConverter, kafkaReceiver, feignPositionClient, errorServiceFacade);

	@Test
	void shouldGetRfData() {
		when(rfDataRepository.findByDataParam(anyDouble(), any(), any(), anyString()))
				.thenReturn(Flux.just(createRfData(1)));

		when(rfDataConverter.rfDataDocumentToDataDto(any()))
				.thenReturn(createDataDto());

		StepVerifier.create(rfDataService.getRfData(getDataParam()))
				.expectSubscription()
				.expectNextMatches(this::isRfDataMatch)
				.verifyComplete();
	}

	@Test
	void shouldSaveRfData() {
		when(rfDataRepository.save(any()))
				.thenReturn(Mono.just(createRfData(1)));

		when(rfDataConverter.dataDtoToRfDataDocument(any()))
				.thenReturn(createRfData(1));

		when(rfDataConverter.rfDataDocumentToDataDto(any()))
				.thenReturn(createDataDto());

		StepVerifier.create(rfDataService.consumeRfData())
				.expectSubscription()
				.expectNextCount(1)
				.verifyComplete();
	}

	@Test
	void getRfDataBySensorName() {
		when(rfDataRepository.findBySensorName(anyString()))
				.thenReturn(Mono.just(createRfData(1)));

		when(rfDataConverter.rfDataDocumentToDataDto(any()))
				.thenReturn(createDataDto());

		StepVerifier.create(rfDataService.getRfDataBySensorName(SENSOR_NAME))
				.expectSubscription()
				.expectNextMatches(this::isRfDataMatch)
				.verifyComplete();

	}

	private boolean isRfDataMatch(DataDto dataDto) {
		return Objects.nonNull(dataDto) &&
				dataDto.getTargetId() == 1 &&
				dataDto.getRssi() == 1d &&
				dataDto.getMiddleFrequency() == 1d &&
				dataDto.getAltitude() == 1d &&
				DateUtil.getRegularDate().equals(dataDto.getTimeAdded()) &&
				Objects.nonNull(dataDto.getSensorDto()) &&
				dataDto.getClientIpAddress().equals(CLIENT_IP);
	}

}