package com.cydeo.service;

import com.cydeo.dto.ClientVendorDto;
import com.cydeo.enums.ClientVendorType;

import java.util.List;

public interface ClientVendorService {

    ClientVendorDto findById(Long id);

    List<ClientVendorDto> listAllClientVendors();

    List<ClientVendorDto> listByClientVendorType(ClientVendorType clientVendorType);

    ClientVendorDto save(ClientVendorDto clientVendorDto);

    void deleteClientVendorById(Long id);

    ClientVendorDto update(ClientVendorDto clientVendorDto);

    boolean isClientVendorExist(ClientVendorDto clientVendorDto);


}
