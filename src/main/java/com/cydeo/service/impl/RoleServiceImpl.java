package com.cydeo.service.impl;

import com.cydeo.dto.RoleDto;
import com.cydeo.entity.Role;
import com.cydeo.exceptions.RoleNotFoundException;
import com.cydeo.repository.RoleRepository;
import com.cydeo.service.RoleService;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final MapperUtil mapperUtil;

    public RoleServiceImpl(RoleRepository roleRepository, MapperUtil mapperUtil) {
        this.roleRepository = roleRepository;
        this.mapperUtil = mapperUtil;
    }


    @Override
    public RoleDto findById(Long id) {
        Role role =roleRepository.findById(id).orElseThrow(() ->new RoleNotFoundException("Role not found"));
        return mapperUtil.convert(role,new RoleDto());
    }

    @Override
    public List<RoleDto> findAllRole() {
        return roleRepository.findAll().stream().map(obj -> mapperUtil.convert(obj,new RoleDto())).collect(Collectors.toList());
    }


}
