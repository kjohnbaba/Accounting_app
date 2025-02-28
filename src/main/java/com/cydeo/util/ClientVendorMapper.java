package com.cydeo.util;

import com.cydeo.dto.ClientVendorDto;
import com.cydeo.entity.ClientVendor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ClientVendorMapper {

    private final ModelMapper modelMapper;

    public ClientVendorMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ClientVendor convertToEntity(ClientVendorDto clientVendorDto) {
        return modelMapper.map(clientVendorDto, ClientVendor.class);
    }

    public ClientVendorDto convertToDto(ClientVendor clientVendor) {
        return modelMapper.map(clientVendor, ClientVendorDto.class);
    }
}
