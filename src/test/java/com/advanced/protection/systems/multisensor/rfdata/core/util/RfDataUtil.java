package com.advanced.protection.systems.multisensor.rfdata.core.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.advanced.protection.systems.multisensor.modelservice.constant.SensorType;
import com.advanced.protection.systems.multisensor.modelservice.domain.Sensor;
import com.advanced.protection.systems.multisensor.modelservice.dto.DataDto;
import com.advanced.protection.systems.multisensor.modelservice.dto.SensorDto;
import com.advanced.protection.systems.multisensor.modelservice.param.DataParam;
import com.advanced.protection.systems.multisensor.rfdata.domain.RfDataDocument;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RfDataUtil {

	public static final String RF_SENSOR_NAME = "rf-sensor-name";
	public static final String CLIENT_IP = "192.168.10.10";

	public static List<RfDataDocument> getRfDataDocuments() {
		return IntStream.rangeClosed(1, 4)
				.mapToObj(RfDataUtil::createRfData)
				.collect(Collectors.toList());
	}

	public static DataDto createDataDto() {
		return DataDto.builder()
				.targetId(1L)
				.middleFrequency(1)
				.rssi(1)
				.altitude(1d)
				.timeAdded(DateUtil.getRegularDate())
				.sensorDto(createSensorDto())
				.clientIpAddress(CLIENT_IP)
				.build();
	}

	public static RfDataDocument createRfData(int value) {
		return RfDataDocument.builder()
				.targetId(String.valueOf(value))
				.middleFrequency(value)
				.rssi(value)
				.altitude(1d)
				.timeAdded(DateUtil.getRegularDate())
				.sensor(createSensor(String.valueOf(value)))
				.clientIpAddress(CLIENT_IP)
				.build();
	}

	public static DataParam getDataParam() {
		return DataParam.builder()
				.lon(1)
				.lat(1)
				.timeAddedFrom(DateUtil.getRegularDateFrom())
				.timeAddedTo(DateUtil.getRegularDateTo())
				.rssi(1)
				.sensorName(RfDataUtil.RF_SENSOR_NAME.concat(String.valueOf(1)))
				.build();
	}

	private static SensorDto createSensorDto(){
		return SensorDto.builder()
				.sensorType(SensorType.RF)
				.name(RF_SENSOR_NAME)
				.configured(true)
				.build();
	}

	private static Sensor createSensor(String name){
		return Sensor.builder()
				.id(1L)
				.sensorType(SensorType.RF)
				.name(RF_SENSOR_NAME.concat(name))
				.configured(true)
				.build();
	}

}
