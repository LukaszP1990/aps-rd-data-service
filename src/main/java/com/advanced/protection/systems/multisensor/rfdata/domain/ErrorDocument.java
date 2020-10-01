package com.advanced.protection.systems.multisensor.rfdata.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Document(collection = "error")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class ErrorDocument {

    @Id
    private String id;
    private String text;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date timeAdded;
}
