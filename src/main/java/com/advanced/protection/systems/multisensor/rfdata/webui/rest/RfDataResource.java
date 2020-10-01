package com.advanced.protection.systems.multisensor.rfdata.webui.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.advanced.protection.systems.multisensor.modelservice.dto.DataDto;
import com.advanced.protection.systems.multisensor.modelservice.param.DataParam;
import com.advanced.protection.systems.multisensor.rfdata.service.data.RfDataServiceFacade;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/rf-data")
public class RfDataResource {

    private final RfDataServiceFacade rfDataServiceFacade;

    public RfDataResource(final RfDataServiceFacade rfDataServiceFacade) {
        this.rfDataServiceFacade = rfDataServiceFacade;
    }

    @GetMapping(value = "/consume", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<DataDto>  consumeRfData(){
        return rfDataServiceFacade.consumeRfData();
    }

    @GetMapping
    public Flux<DataDto> download(DataParam dataParam) {
        log.info("download by dataParam: {}", dataParam);
        return rfDataServiceFacade.getRfData(dataParam);
    }

    @GetMapping(path = "/sensor-name/{sensor-name}")
    public Mono<DataDto> downloadBySensorName(@RequestParam(name = "sensor-name") String sensorName) {
        log.info("downloadBySensorName - sensor-name: {}", sensorName);
        return rfDataServiceFacade.getRfDataBySensorName(sensorName);
    }
}
