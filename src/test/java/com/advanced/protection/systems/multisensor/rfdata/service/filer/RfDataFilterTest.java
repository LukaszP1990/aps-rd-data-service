package com.advanced.protection.systems.multisensor.rfdata.service.filer;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.advanced.protection.systems.multisensor.modelservice.constant.SensorType;
import com.advanced.protection.systems.multisensor.modelservice.dto.DataDto;
import com.advanced.protection.systems.multisensor.rfdata.core.util.RfDataUtil;

class RfDataFilterTest {

	private static final DataDto dataDto = RfDataUtil.createDataDto();

	@Test
	void shouldReturnTrueWhenSensorTypeIsSetToRf() {
		assertTrue(RfDataFilter.isSensorByType(dataDto, SensorType.RF));
	}

	@Test
	void shouldReturnFalseWhenSensorTypeIsSetToPosition() {
		assertFalse(RfDataFilter.isSensorByType(dataDto, SensorType.POSITION));
	}

	@Test
	void shouldReturnTrueWhenAllFieldsAreNotNull() {
		assertTrue(RfDataFilter.areRfDataFieldsNotNull(dataDto));
	}

	@Test
	void shouldReturnFalseWhenAnyFieldIsNull() {
		assertFalse(RfDataFilter.areRfDataFieldsNotNull(
				DataDto.builder().build())
		);
	}
}