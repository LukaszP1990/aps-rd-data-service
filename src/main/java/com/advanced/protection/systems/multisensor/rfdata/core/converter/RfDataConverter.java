package com.advanced.protection.systems.multisensor.rfdata.core.converter;

import java.util.UUID;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.advanced.protection.systems.multisensor.modelservice.constant.DataType;
import com.advanced.protection.systems.multisensor.modelservice.constant.SensorType;
import com.advanced.protection.systems.multisensor.modelservice.domain.Sensor;
import com.advanced.protection.systems.multisensor.modelservice.dto.DataDto;
import com.advanced.protection.systems.multisensor.modelservice.dto.SensorDto;
import com.advanced.protection.systems.multisensor.rfdata.domain.RfDataDocument;

@Mapper
public interface RfDataConverter {

    @Mapping(target = "lat", ignore = true)
    @Mapping(target = "lon", ignore = true)
    @Mapping(target = "sensorDto", source = "sensor", qualifiedByName = "setSensorDto")
    DataDto rfDataDocumentToDataDto(RfDataDocument rfDataDocument);

    @Mapping(target = "targetId", qualifiedByName = "convertTargetIdToString")
    @Mapping(target = "sensor", source = "sensorDto", qualifiedByName = "setSensor")
    RfDataDocument dataDtoToRfDataDocument(DataDto dataDto);

    @Named("convertTargetIdToString")
    default String convertTargetIdToString(Long targetId) {
        return String.valueOf(targetId);
    }

    @Named("setSensorDto")
    default SensorDto setSensorDto(Sensor sensor) {
        return SensorDto.builder()
        .configured(sensor.isConfigured())
        .name(sensor.getName())
        .sensorType(SensorType.RF).build();
    }

    @Named("setSensor")
    default Sensor setSensor(SensorDto sensorDto) {
        return Sensor.builder()
                .id(UUID.randomUUID().getMostSignificantBits())
                .configured(sensorDto.isConfigured())
                .name(sensorDto.getName())
                .sensorType(SensorType.RF)
                .build();
    }

}
