package com.cydeo.repository;

import com.cydeo.entity.ClientVendor;
import com.cydeo.enums.ClientVendorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientVendorRepository extends JpaRepository<ClientVendor, Long> {

    List<ClientVendor> findAllByIsDeleted(Boolean isDeleted);
    List<ClientVendor> findAllByClientVendorTypeOrderByClientVendorName(ClientVendorType clientVendorType);
    List<ClientVendor> findAllByCompany_Id_AndIsDeleted(Long companyId, boolean isDeleted);
    List<ClientVendor> findAllByClientVendorTypeAndCompany_IdOrderByClientVendorName(ClientVendorType clientVendorType, Long companyId);
    List<ClientVendor> findAllByCompany_Id(Long companyId);
    Optional<ClientVendor> findByClientVendorName_AndCompany_Id(String clientVendorName, Long companyId);
}

