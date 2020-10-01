package com.advanced.protection.systems.multisensor.rfdata.webui.rest;

import static com.advanced.protection.systems.multisensor.rfdata.core.util.RfDataUtil.RF_SENSOR_NAME;
import static com.advanced.protection.systems.multisensor.rfdata.core.util.RfDataUtil.getDataParam;
import static com.advanced.protection.systems.multisensor.rfdata.core.util.RfDataUtil.getRfDataDocuments;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.advanced.protection.systems.multisensor.modelservice.constant.DataType;
import com.advanced.protection.systems.multisensor.modelservice.dto.DataDto;
import com.advanced.protection.systems.multisensor.rfdata.AbstractIntegrationTest;
import com.advanced.protection.systems.multisensor.rfdata.dao.RfDataRepository;

import reactor.core.publisher.Flux;

class RfDataResourceTest extends AbstractIntegrationTest {

	@Autowired
	private RfDataRepository rfDataRepository;

	@BeforeEach
	void setUp() {
		rfDataRepository.deleteAll().thenMany(Flux.fromIterable(getRfDataDocuments()))
				.flatMap(rfDataDocument -> rfDataRepository.save(rfDataDocument))
				.doOnNext(rfDataDocument -> System.out.println("Inserted rfDataDocument: " + rfDataDocument))
				.blockLast();
	}

	@Test
	void shouldDownloadByDataParam() {
		var dataParam = getDataParam();
		webTestClient.get().
				uri(uriBuilder ->
						uriBuilder
								.path("/api/rf-data")
								.queryParam("lon", dataParam.getLon())
								.queryParam("lat", dataParam.getLat())
								.queryParam("timeAddedFrom", dataParam.getTimeAddedFrom())
								.queryParam("timeAddedTo", dataParam.getTimeAddedTo())
								.queryParam("rssi", dataParam.getRssi())
								.queryParam("sensorName", dataParam.getSensorName())
								.build())
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(DataDto.class)
				.hasSize(4)
				.consumeWith(response ->
						Objects.requireNonNull(response.getResponseBody())
								.forEach(dataDto ->
										assertEquals(DataType.RF, dataDto.getDataType()))
				);
	}

	@Test
	void downloadBySensorName() {
		var sensorName = RF_SENSOR_NAME.concat(String.valueOf(1));
		webTestClient.get().
				uri(uriBuilder ->
						uriBuilder
								.path("/api/rf-data")
								.queryParam("sensorName", sensorName)
								.build())
				.exchange()
				.expectStatus().isOk()
				.expectBody(DataDto.class)
				.consumeWith(response ->
						assertEquals(DataType.RF, Objects.requireNonNull(response.getResponseBody()).getDataType())
				);
	}

}