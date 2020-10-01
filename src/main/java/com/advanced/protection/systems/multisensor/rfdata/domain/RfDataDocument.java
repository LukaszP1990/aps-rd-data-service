package com.advanced.protection.systems.multisensor.rfdata.domain;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.advanced.protection.systems.multisensor.modelservice.domain.Sensor;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "rfdata")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class RfDataDocument {

    @Id
    private String targetId;
    private double middleFrequency;
    private double rssi;
    private Double altitude;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date timeAdded;
    private Sensor sensor;
    private String clientIpAddress;
}
