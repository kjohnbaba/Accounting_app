package com.cydeo.service;

import com.cydeo.dto.ClientVendorDto;
import com.cydeo.dto.CompanyDto;
import com.cydeo.enums.ClientVendorType;

import javax.validation.Valid;
import java.util.List;

public interface ClientVendorService {

    ClientVendorDto findById(Long id);

    List<ClientVendorDto> findAll();

    void update(Long id, ClientVendorDto clientVendorDto);

    void delete(Long clientVendorId);

    void save(ClientVendorDto clientVendorDto);

    List<ClientVendorDto> getClientVendorListByLoggedInUser();

    List<ClientVendorDto> listAllByClientVendorType(ClientVendorType clientVendorType);

    boolean isClientVendorExist(@Valid ClientVendorDto clientVendorDto);

    CompanyDto getCompanyByLoggedInUser();
}

