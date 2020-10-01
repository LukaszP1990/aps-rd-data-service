package com.advanced.protection.systems.multisensor.rfdata.core.util;

import com.advanced.protection.systems.multisensor.modelservice.constant.ErrorType;
import com.advanced.protection.systems.multisensor.modelservice.dto.ErrorDto;
import com.advanced.protection.systems.multisensor.modelservice.param.ErrorParam;
import com.advanced.protection.systems.multisensor.rfdata.domain.ErrorDocument;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorUtil {

	public static List<ErrorDocument> getErrors() {
		return IntStream.rangeClosed(1, 4)
				.mapToObj(ErrorUtil::createError)
				.collect(Collectors.toList());
	}

	public static ErrorDto createErrorDto() {
		return ErrorDto.builder()
				.text(ErrorType.DATA_SAVE_ERROR.getText())
				.timeAdded(DateUtil.getRegularDate())
				.build();
	}

	public static ErrorDocument createError(int value) {
		return ErrorDocument.builder()
				.id(String.valueOf(value))
				.text(ErrorType.DATA_SAVE_ERROR.getText())
				.timeAdded(DateUtil.getRegularDate())
				.build();
	}

	public static ErrorParam getErrorParam() {
		return ErrorParam.builder()
				.timeAddedFrom(DateUtil.getRegularDateFrom())
				.timeAddedTo(DateUtil.getRegularDateTo())
				.type(ErrorType.DATA_SAVE_ERROR.getText())
				.build();
	}
}
