package com.cydeo.service.impl;

import com.cydeo.dto.ClientVendorDto;
import com.cydeo.dto.CompanyDto;
import com.cydeo.entity.Address;
import com.cydeo.entity.ClientVendor;
import com.cydeo.entity.Company;
import com.cydeo.enums.ClientVendorType;
import com.cydeo.repository.ClientVendorRepository;
import com.cydeo.service.AddressService;
import com.cydeo.service.ClientVendorService;
import com.cydeo.service.CompanyService;
import com.cydeo.service.SecurityService;
import com.cydeo.util.AddressMapper;
import com.cydeo.util.ClientVendorMapper;
import com.cydeo.util.CompanyMapper;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientVendorServiceImpl implements ClientVendorService {

    MapperUtil mapperUtil;
    ClientVendorRepository clientVendorRepository;
    ClientVendorMapper clientVendorMapper;
    AddressMapper addressMapper;
    CompanyMapper companyMapper;
    AddressService addressService;
    SecurityService securityService;
    CompanyService companyService;

    public ClientVendorServiceImpl(MapperUtil mapperUtil, ClientVendorRepository clientVendorRepository, ClientVendorMapper clientVendorMapper, AddressMapper addressMapper, CompanyMapper companyMapper, AddressService addressService, SecurityService securityService, CompanyService companyService) {
        this.mapperUtil = mapperUtil;
        this.clientVendorRepository = clientVendorRepository;
        this.clientVendorMapper = clientVendorMapper;
        this.addressMapper = addressMapper;
        this.companyMapper = companyMapper;
        this.addressService = addressService;
        this.securityService = securityService;
        this.companyService = companyService;
    }

    @Override
    public ClientVendorDto findById(Long id) {
        return mapperUtil.convert(clientVendorRepository.findById(id).get(), new ClientVendorDto());
    }

    @Override
    public List<ClientVendorDto> findAll() {

        return clientVendorRepository.findAllByIsDeleted(false)
                .stream()
                .map(clientVendorMapper::convertToDto)
                .collect(Collectors.toList());
    }

    public void update(Long id, ClientVendorDto clientVendorDto){
        ClientVendor existingClientVendor = clientVendorRepository.findById(id)
                .orElseThrow();

        if (clientVendorDto.getClientVendorName() != null) {
            existingClientVendor.setClientVendorName(clientVendorDto.getClientVendorName());
        }
        if (clientVendorDto.getAddress() != null) {
            Address address = addressMapper.convertToEntity(clientVendorDto.getAddress());
            addressService.save(address);
            existingClientVendor.setAddress(address);
        }
        if (clientVendorDto.getPhone() != null) {
            existingClientVendor.setPhone(clientVendorDto.getPhone());
        }
        if (clientVendorDto.getWebsite() != null) {
            existingClientVendor.setWebsite(clientVendorDto.getWebsite());
        }
        if (clientVendorDto.getCompany() != null) {
            Company company = companyMapper.convertToEntity(clientVendorDto.getCompany());
            existingClientVendor.setCompany(company);
        }
        if (clientVendorDto.getClientVendorType() != null) {
            existingClientVendor.setClientVendorType(clientVendorDto.getClientVendorType());
        }
        clientVendorRepository.save(existingClientVendor);
    }

    @Override
    public void delete(Long clientVendorId) {
        Optional<ClientVendor> clientVendor = clientVendorRepository.findById(clientVendorId);
        clientVendor.ifPresent(vendor -> vendor.setIsDeleted(true));
        clientVendorRepository.save(clientVendor.get());
    }

    @Override
    public List<ClientVendorDto> listAllByClientVendorType(ClientVendorType clientVendorType) {
        List<ClientVendor> list = clientVendorRepository.findAllByClientVendorTypeAndCompany_IdOrderByClientVendorName(clientVendorType, companyService.getCompanyIdByLoggedInUser());
        return list.stream().map(each -> mapperUtil.convert(each,new ClientVendorDto())).toList();
    }

    @Override
    public boolean isClientVendorExist(ClientVendorDto clientVendorDto) {
        Long companyId = companyService.getCompanyIdByLoggedInUser();
        return clientVendorRepository.findByClientVendorName_AndCompany_Id(clientVendorDto.getClientVendorName(), companyId)
                .map(clientVendor -> !Objects.equals(clientVendorDto.getId(), clientVendor.getId()))
                .orElse(false);

    }

    @Override
    public void save(ClientVendorDto clientVendorDto) {
        clientVendorDto.setCompany(getCompanyByLoggedInUser());
        ClientVendor clientVendor = clientVendorMapper.convertToEntity(clientVendorDto);
        clientVendor.setId(clientVendorDto.getId());
        clientVendorRepository.save(clientVendor);
    }

    public List<ClientVendorDto> getClientVendorListByLoggedInUser() {
        Long companyId = securityService.getLoggedInUser().getCompany().getId();
        return clientVendorRepository.findAllByCompany_Id_AndIsDeleted(companyId, false).stream().map(clientVendorMapper::convertToDto)
                .collect(Collectors.toList());
    }

    public CompanyDto getCompanyByLoggedInUser(){
        return securityService.getLoggedInUser().getCompany();
    }


}
