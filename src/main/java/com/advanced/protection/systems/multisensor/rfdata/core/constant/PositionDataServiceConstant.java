package com.advanced.protection.systems.multisensor.rfdata.core.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PositionDataServiceConstant {

    private static final String SERVICE_NAME = "multisensorposition";
    public static final String POSITION_DATA_RESOURCE = SERVICE_NAME + "/api";
}
