package com.cydeo.util;

import com.cydeo.dto.AddressDto;
import com.cydeo.entity.Address;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    private final ModelMapper modelMapper;

    public AddressMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Address convertToEntity(AddressDto addressDto) {
        return modelMapper.map(addressDto, Address.class);
    }

    public AddressDto convertToDto(Address address) {
        return modelMapper.map(address, AddressDto.class);
    }
}
