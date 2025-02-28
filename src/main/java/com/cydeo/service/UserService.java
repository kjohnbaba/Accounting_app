
package com.cydeo.service;

import com.cydeo.dto.CompanyDto;
import com.cydeo.dto.CurrencyRateDto;
import com.cydeo.dto.UserDto;
import org.springframework.stereotype.Service;

import com.cydeo.entity.User;
import java.util.List;


public interface UserService {

    UserDto findByUsername(String username);


    void save(UserDto userDto);
    void update(UserDto userDto);
    void deleteById(Long userId);
    UserDto findById(Long id);
    List<UserDto> listAllUsers();
    Integer getNumberOfAdmins(CompanyDto companyDto);

}