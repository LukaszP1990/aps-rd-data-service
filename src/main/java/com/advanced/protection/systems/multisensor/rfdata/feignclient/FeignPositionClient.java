package com.advanced.protection.systems.multisensor.rfdata.feignclient;

import com.advanced.protection.systems.multisensor.modelservice.dto.DataDto;
import com.advanced.protection.systems.multisensor.rfdata.core.constant.PositionDataServiceConstant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(name = PositionDataServiceConstant.POSITION_DATA_RESOURCE)
public interface FeignPositionClient {

    @GetMapping(path = "/position-data/sensor-name/{sensor-name}")
    Mono<DataDto> downloadBySensorName(@RequestParam(name = "sensor-name") String sensorName);
}
