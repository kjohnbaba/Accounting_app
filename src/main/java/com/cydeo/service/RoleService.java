package com.cydeo.service;

import com.cydeo.dto.RoleDto;

import java.util.List;


public interface RoleService {


    RoleDto findById(Long id);
    List<RoleDto> findAllRole();

}
