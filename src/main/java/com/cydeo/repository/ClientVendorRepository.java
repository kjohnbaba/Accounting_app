package com.cydeo.repository;

import com.cydeo.entity.ClientVendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientVendorRepository extends JpaRepository<ClientVendor, Long> {

    List<ClientVendor> findClientVendorsByCompany_Id(Long companyId);

    Optional<ClientVendor> findByClientVendorName_AndCompany_Id(String clientVendorName, Long companyId);

}
