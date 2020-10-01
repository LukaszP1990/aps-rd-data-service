package com.advanced.protection.systems.multisensor.rfdata.service.data;

import com.advanced.protection.systems.multisensor.modelservice.dto.DataDto;
import com.advanced.protection.systems.multisensor.modelservice.param.DataParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.ReceiverRecord;

public interface RfDataServiceFacade {

    Flux<DataDto> getRfData(DataParam dataParam);

    Flux<DataDto>  consumeRfData();

    Mono<DataDto> getRfDataBySensorName(String name);
}
