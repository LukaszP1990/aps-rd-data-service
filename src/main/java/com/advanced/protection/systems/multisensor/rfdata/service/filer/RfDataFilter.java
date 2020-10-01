package com.advanced.protection.systems.multisensor.rfdata.service.filer;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.advanced.protection.systems.multisensor.modelservice.constant.SensorType;
import com.advanced.protection.systems.multisensor.modelservice.dto.DataDto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RfDataFilter {

    public static boolean isSensorByType(DataDto dataDto,
                                         SensorType type) {
        var sensorType = dataDto.getSensorDto().getSensorType();
        return Objects.nonNull(sensorType) && type.equals(sensorType);
    }

    static boolean areRfDataFieldsNotNull(DataDto rfDataDto) {
        var rfDataFields = Stream.of(
                rfDataDto.getTargetId(),
                rfDataDto.getLat(),
                rfDataDto.getLon(),
                rfDataDto.getAltitude(),
                rfDataDto.getTimeAdded(),
                rfDataDto.getSensorDto()
        ).collect(Collectors.toList());
        return validateFields(rfDataFields);
    }

    private static <T> boolean validateFields(List<T> dataFields) {
        return Optional.ofNullable(dataFields)
                .map(field -> isField(dataFields))
                .orElse(false);
    }

    private static <T> boolean isField(List<T> dataFields) {
        return IntStream.rangeClosed(0, dataFields.size() - 1)
                .allMatch(value -> Objects.nonNull(dataFields.get(value)));
    }

}
