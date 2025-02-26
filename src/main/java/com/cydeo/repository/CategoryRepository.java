package com.cydeo.repository;

import com.cydeo.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByDescriptionAndCompany_Id(String description, Long companyId);

    List<Category> findByCompany_Id(Long id);

}
