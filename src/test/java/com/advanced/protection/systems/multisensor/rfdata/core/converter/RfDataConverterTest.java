package com.advanced.protection.systems.multisensor.rfdata.core.converter;

import static com.advanced.protection.systems.multisensor.rfdata.core.util.RfDataUtil.CLIENT_IP;
import static com.advanced.protection.systems.multisensor.rfdata.core.util.RfDataUtil.RF_SENSOR_NAME;
import static com.advanced.protection.systems.multisensor.rfdata.core.util.RfDataUtil.createDataDto;
import static com.advanced.protection.systems.multisensor.rfdata.core.util.RfDataUtil.createRfData;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.advanced.protection.systems.multisensor.modelservice.constant.SensorType;
import com.advanced.protection.systems.multisensor.modelservice.dto.DataDto;
import com.advanced.protection.systems.multisensor.rfdata.domain.RfDataDocument;

class RfDataConverterTest {

	private static final DataDto dataDto = createDataDto();
	private static final RfDataDocument rfData = createRfData(1);
	private RfDataConverter rfDataConverter = Mappers.getMapper(RfDataConverter.class);

	@Test
	void shouldConvertRfDataDocumentToDataDto() {
		var dataDto = rfDataConverter.rfDataDocumentToDataDto(rfData);

		assertNotNull(dataDto);
		assertAll(() -> {
			assertEquals(1, dataDto.getMiddleFrequency());
			assertEquals(1, dataDto.getRssi());
			assertEquals(1, dataDto.getAltitude());
			assertEquals(CLIENT_IP, dataDto.getClientIpAddress());
			assertNotNull(dataDto.getTimeAdded());
			assertNotNull(dataDto.getSensorDto());
			assertEquals(RF_SENSOR_NAME, dataDto.getSensorDto().getName());
			assertEquals(SensorType.RF, dataDto.getSensorDto().getSensorType());
			assertTrue(dataDto.getSensorDto().isConfigured());
		});
	}

	@Test
	void shouldConvertDataDtoToRfDataDocument() {
		var rfDataDocument = rfDataConverter.dataDtoToRfDataDocument(dataDto);

		assertNotNull(rfDataDocument);
		assertAll(() -> {
			assertEquals(1, rfDataDocument.getMiddleFrequency());
			assertEquals(1, rfDataDocument.getRssi());
			assertEquals(1, rfDataDocument.getAltitude());
			assertEquals(CLIENT_IP, rfDataDocument.getClientIpAddress());
			assertNotNull(rfDataDocument.getTimeAdded());
			assertNotNull(rfDataDocument.getSensor());
			assertNotNull(rfDataDocument.getSensor().getId());
			assertEquals(RF_SENSOR_NAME, rfDataDocument.getSensor().getName());
			assertEquals(SensorType.RF, rfDataDocument.getSensor().getSensorType());
			assertTrue(rfDataDocument.getSensor().isConfigured());
		});
	}
}