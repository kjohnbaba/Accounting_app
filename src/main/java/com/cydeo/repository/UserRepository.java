package com.cydeo.repository;

import com.cydeo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    List<User> findAllByIsDeletedFalse();
    List<User> findAllByCompany_IdAndIsDeletedFalse(Long companyId);
    List<User> findAllByCompany_IdAndRole_IdAndIsDeletedFalse(Long companyId, Long roleId);
}
