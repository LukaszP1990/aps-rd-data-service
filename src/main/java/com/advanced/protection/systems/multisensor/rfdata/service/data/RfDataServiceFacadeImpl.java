package com.advanced.protection.systems.multisensor.rfdata.service.data;

import com.advanced.protection.systems.multisensor.modelservice.dto.DataDto;
import com.advanced.protection.systems.multisensor.modelservice.param.DataParam;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.ReceiverRecord;

@Component
public class RfDataServiceFacadeImpl implements RfDataServiceFacade {

    private final RfDataService rfDataService;

    public RfDataServiceFacadeImpl(RfDataService rfDataService) {
        this.rfDataService = rfDataService;
    }

    @Override
    public Flux<DataDto> getRfData(DataParam dataParam) {
        return rfDataService.getRfData(dataParam);
    }

    @Override
    public Flux<DataDto> consumeRfData() {
        return rfDataService.consumeRfData();
    }

    @Override
    public Mono<DataDto> getRfDataBySensorName(String name) {
        return rfDataService.getRfDataBySensorName(name);
    }
}
