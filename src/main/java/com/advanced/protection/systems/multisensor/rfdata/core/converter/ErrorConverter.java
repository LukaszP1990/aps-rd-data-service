package com.advanced.protection.systems.multisensor.rfdata.core.converter;

import com.advanced.protection.systems.multisensor.modelservice.dto.ErrorDto;
import com.advanced.protection.systems.multisensor.rfdata.domain.ErrorDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ErrorConverter {

    @Mapping(target = "id", ignore = true)
    ErrorDocument errorDtoToErrorDocument(ErrorDto errorDto);

    ErrorDto errorDocumentToErrorDto(ErrorDocument errorDocument);
}
