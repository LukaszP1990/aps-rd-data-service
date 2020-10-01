package com.advanced.protection.systems.multisensor.rfdata.dao;

import static com.advanced.protection.systems.multisensor.rfdata.core.util.RfDataUtil.RF_SENSOR_NAME;
import static com.advanced.protection.systems.multisensor.rfdata.core.util.RfDataUtil.getRfDataDocuments;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import com.advanced.protection.systems.multisensor.rfdata.core.util.DateUtil;
import com.advanced.protection.systems.multisensor.rfdata.domain.RfDataDocument;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@DataMongoTest
@ExtendWith(value = MockitoExtension.class)
@ActiveProfiles("test")
class RfDataRepositoryTest {

	private static final String OK_SENSOR_NAME = RF_SENSOR_NAME.concat(String.valueOf(1));
	private static final String BAD_SENSOR_NAME = RF_SENSOR_NAME.concat(String.valueOf(5));
	private static List<RfDataDocument> rfDataDocuments = getRfDataDocuments();

	@Autowired
	private RfDataRepository rfDataRepository;

	@BeforeEach
	void setUp() {
		rfDataRepository.deleteAll()
				.thenMany(Flux.fromIterable(rfDataDocuments))
				.flatMap(rfDataRepository::save)
				.doOnNext(item -> System.out.println("Inserted:" + item.toString()))
				.blockLast();
	}

	@Test
	void shouldGetRfDataDocumentBySensorName() {
		StepVerifier.create(rfDataRepository.findBySensorName(OK_SENSOR_NAME))
				.expectSubscription()
				.expectNextCount(1)
				.verifyComplete();
	}

	@Test
	void shouldNotGetRfDataDocumentBySensorName() {
		StepVerifier.create(rfDataRepository.findBySensorName(BAD_SENSOR_NAME))
				.expectSubscription()
				.verifyComplete();
	}

	@Test
	void shouldGetRfDataDocumentsByDataParam() {
		StepVerifier.create(rfDataRepository.findByDataParam(1, DateUtil.getRegularDateFrom(), DateUtil.getRegularDateTo(), OK_SENSOR_NAME))
				.expectSubscription()
				.expectNextCount(1)
				.verifyComplete();
	}

	@Test
	void shouldNotGetRfDataDocumentsByDataParam() {
		StepVerifier.create(rfDataRepository.findByDataParam(1, new Date(), new Date(), BAD_SENSOR_NAME))
				.expectSubscription()
				.verifyComplete();
	}
}