package com.cydeo.repository;

import com.cydeo.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByCompany_IdAndIsDeletedFalseOrderByDescriptionAsc(Long companyId);
    List<Category> findAllByIsDeletedFalse();
    Category findCategoryById (Long id);
}
