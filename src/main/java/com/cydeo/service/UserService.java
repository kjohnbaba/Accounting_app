package com.cydeo.service;

import com.cydeo.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto findByUsername(String username);

    List<UserDto> listAllByLoggedInUser();

    UserDto findById(Long id);

    UserDto update(UserDto userDto);

    void deleteById(Long id);

    UserDto save(UserDto user);

    boolean isUsernameExist(UserDto userDto);

}
