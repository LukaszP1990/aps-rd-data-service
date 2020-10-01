package com.advanced.protection.systems.multisensor.rfdata.webui.rest;

import static com.advanced.protection.systems.multisensor.rfdata.core.util.ErrorUtil.getErrorParam;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.advanced.protection.systems.multisensor.modelservice.constant.ErrorType;
import com.advanced.protection.systems.multisensor.modelservice.dto.ErrorDto;
import com.advanced.protection.systems.multisensor.rfdata.AbstractIntegrationTest;
import com.advanced.protection.systems.multisensor.rfdata.core.util.ErrorUtil;
import com.advanced.protection.systems.multisensor.rfdata.core.util.RfDataUtil;
import com.advanced.protection.systems.multisensor.rfdata.dao.ErrorRepository;
import com.advanced.protection.systems.multisensor.rfdata.feignclient.FeignPositionClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

class ErrorResourceTest extends AbstractIntegrationTest {

	@Autowired
	private ErrorRepository errorRepository;

	@MockBean
	private FeignPositionClient feignPositionClient;

	@BeforeEach
	void setUp() {
		errorRepository.deleteAll().thenMany(Flux.fromIterable(ErrorUtil.getErrors()))
				.flatMap(errorDocument -> errorRepository.save(errorDocument))
				.doOnNext(errorDocument -> System.out.println("Inserted errorDocument: " + errorDocument))
				.blockLast();
	}

	@Test
	void shouldGetErrorsByErrorParam() {
		var errorParam = getErrorParam();
		webTestClient.get().
				uri(uriBuilder ->
						uriBuilder
								.path("/api/rf-data/errors")
								.queryParam("type", errorParam.getType())
								.queryParam("timeAddedFrom", errorParam.getTimeAddedFrom())
								.queryParam("timeAddedTo", errorParam.getTimeAddedTo())
								.build())
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(ErrorDto.class)
				.hasSize(4)
				.consumeWith(response ->
						Objects.requireNonNull(response.getResponseBody())
								.forEach(errorDto ->
										assertEquals(ErrorType.DATA_SAVE_ERROR.getText(), errorDto.getText()))
				);
	}

	@Test
	void shouldConsumeRfData() {
		when(feignPositionClient.downloadBySensorName(anyString()))
				.thenReturn(Mono.empty());

		webTestClient.get()
				.uri("/api/rf-data/consume")
				.exchange()
				.expectStatus().isOk();
	}

}