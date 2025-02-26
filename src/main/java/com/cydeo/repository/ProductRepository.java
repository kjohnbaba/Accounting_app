package com.cydeo.repository;


import com.cydeo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.name = ?1 AND p.category.company.id = ?2")
    Product findProductByNameAndCompanyId(String name, Long companyId);

    @Query("SELECT p FROM Product p WHERE p.category.company.id = ?1")
    List<Product> findByCompanyId(Long companyId);


}
