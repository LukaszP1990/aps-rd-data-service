package com.advanced.protection.systems.multisensor.rfdata.service.handler;

import static org.mockito.Mockito.*;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import com.advanced.protection.systems.multisensor.rfdata.AbstractIntegrationTest;
import com.advanced.protection.systems.multisensor.rfdata.RfDataServiceApplication;
import com.advanced.protection.systems.multisensor.rfdata.core.util.RfDataUtil;
import com.advanced.protection.systems.multisensor.rfdata.dao.RfDataRepository;
import com.advanced.protection.systems.multisensor.rfdata.feignclient.FeignPositionClient;
import com.advanced.protection.systems.multisensor.rfdata.service.data.RfDataService;
import com.advanced.protection.systems.multisensor.rfdata.service.data.RfDataServiceFacade;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


class RfDataHandlerTest extends AbstractIntegrationTest {

	@Autowired
	private RfDataRepository rfDataRepository;

	@MockBean
	private FeignPositionClient feignPositionClient;

	@BeforeEach
	void setUp() {
		rfDataRepository.deleteAll().thenMany(Flux.fromIterable(RfDataUtil.getRfDataDocuments()))
				.flatMap(rfDataDocument -> rfDataRepository.save(rfDataDocument))
				.doOnNext(rfDataDocument -> System.out.println("Inserted rfDataDocument: " + rfDataDocument))
				.blockLast();
	}

	@Test
	void shouldSaveRfData() {
		when(feignPositionClient.downloadBySensorName(anyString()))
				.thenReturn(Mono.empty());

		var rfDataDocument = RfDataUtil.createRfData(100);
		webTestClient.post()
				.uri(uriBuilder ->
						uriBuilder
								.path("/api/rf-data")
								.build())
				.bodyValue(rfDataDocument)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody();
	}
}