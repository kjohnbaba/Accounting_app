package com.cydeo.repository;

import com.cydeo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    List<User> findAllByRole_Id(long roleId);

    List<User> findAllByCompany_Id(Long companyId);

    Integer countAllByCompany_IdAndRole_Description(Long companyId, String roleDescription);

}
