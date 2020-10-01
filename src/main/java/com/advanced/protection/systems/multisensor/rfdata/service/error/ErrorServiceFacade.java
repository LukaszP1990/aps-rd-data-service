package com.advanced.protection.systems.multisensor.rfdata.service.error;

import com.advanced.protection.systems.multisensor.modelservice.constant.ErrorType;
import com.advanced.protection.systems.multisensor.modelservice.dto.ErrorDto;
import com.advanced.protection.systems.multisensor.modelservice.param.ErrorParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ErrorServiceFacade {

    Flux<ErrorDto> getErrors(ErrorParam errorParam);

    Mono<ErrorDto> saveErrorDto(ErrorType errorType);
}
