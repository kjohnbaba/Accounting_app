package com.cydeo.service.impl;

import com.cydeo.dto.ClientVendorDto;
import com.cydeo.entity.ClientVendor;
import com.cydeo.enums.ClientVendorType;
import com.cydeo.exception.ClientVendorNotFoundException;
import com.cydeo.repository.ClientVendorRepository;
import com.cydeo.service.ClientVendorService;
import com.cydeo.service.CompanyService;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class ClientVendorServiceImpl implements ClientVendorService {

    private final MapperUtil mapperUtil;
    private final ClientVendorRepository clientVendorRepository;
    private final CompanyService companyService;

    public ClientVendorServiceImpl(MapperUtil mapperUtil, ClientVendorRepository clientVendorRepository, CompanyService companyService) {
        this.mapperUtil = mapperUtil;
        this.clientVendorRepository = clientVendorRepository;
        this.companyService = companyService;
    }

    @Override
    @Transactional(readOnly = true)
    public ClientVendorDto findById(Long id) {
        ClientVendor clientVendor = clientVendorRepository.findById(id).orElseThrow(ClientVendorNotFoundException::new);
        return mapperUtil.convert(clientVendor, new ClientVendorDto());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientVendorDto> listAllClientVendors() {
        Long companyId = companyService.getCompanyDtoByLoggedInUser().getId();
        List<ClientVendor> clientVendorList = clientVendorRepository.findClientVendorsByCompany_Id(companyId);
        return clientVendorList.stream()
//                .filter(clientVendor -> clientVendor.getCompany().getId().equals(companyService.getCompanyDtoByLoggedInUser().getId()))
                .map(cv -> {
                    ClientVendorDto dto = mapperUtil.convert(cv, new ClientVendorDto());
                    dto.setHasInvoice(!cv.getInvoices().isEmpty());
                    return dto;
                })
                .sorted(Comparator.comparing(ClientVendorDto::getClientVendorType).reversed()
                        .thenComparing(ClientVendorDto::getClientVendorName))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientVendorDto> listByClientVendorType(ClientVendorType clientVendorType) {
        Long companyId = companyService.getCompanyDtoByLoggedInUser().getId();
        return clientVendorRepository.findAll()
                .stream()
                .filter(clientOrVendor -> clientOrVendor.getClientVendorType().equals(clientVendorType))
                .filter(companySelect -> companySelect.getCompany().getId().equals(companyId))
                .map(vendor -> mapperUtil.convert(vendor, new ClientVendorDto()))
                .toList();
    }

    @Override
    public ClientVendorDto save(ClientVendorDto clientVendorDto) {
        clientVendorDto.setCompany(companyService.getCompanyDtoByLoggedInUser());
        ClientVendor clientVendor = mapperUtil.convert(clientVendorDto, new ClientVendor());
        ClientVendor saved = clientVendorRepository.save(clientVendor);
        return mapperUtil.convert(saved, new ClientVendorDto());
    }

    @Override
    public ClientVendorDto update(ClientVendorDto clientVendorDto) {
        ClientVendor saved = clientVendorRepository.findById(clientVendorDto.getId()).orElseThrow(ClientVendorNotFoundException::new);
        clientVendorDto.getAddress().setId(saved.getAddress().getId());     // otherwise it creates new address instead of updating existing one
        clientVendorDto.setCompany(companyService.getCompanyDtoByLoggedInUser());
        ClientVendor updated = mapperUtil.convert(clientVendorDto, new ClientVendor());
        return mapperUtil.convert(clientVendorRepository.save(updated), new ClientVendorDto());
    }

    @Override
    public void deleteClientVendorById(Long id) {
        ClientVendor clientVendor = clientVendorRepository.findById(id).orElseThrow(ClientVendorNotFoundException::new);
        clientVendor.setIsDeleted(true);
        clientVendor.getAddress().setIsDeleted(true);
        clientVendor.setClientVendorName(clientVendor.getClientVendorName() + "-" + clientVendor.getId());
        clientVendorRepository.save(clientVendor);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isClientVendorExist(ClientVendorDto clientVendorDto) {
        Long companyId = companyService.getCompanyDtoByLoggedInUser().getId();
        return clientVendorRepository.findByClientVendorName_AndCompany_Id(clientVendorDto.getClientVendorName(), companyId)
                .map(clientVendor -> !Objects.equals(clientVendorDto.getId(), clientVendor.getId()))
                .orElse(false);
    }

    // same result :
//    public boolean isClientVendorExist2(ClientVendorDto clientVendorDto) {
//        Long companyId = companyService.getCompanyDtoByLoggedInUser().getId();
//        ClientVendor clientVendor = clientVendorRepository.findByClientVendorName_AndCompany_Id(
//                        clientVendorDto.getClientVendorName(), companyId)
//                .orElse(null);
//        if (clientVendor == null) return false; // Objects.equals() does same thing
//
//        return !Objects.equals(clientVendorDto.getId(), clientVendor.getId());
//    }


}






