package com.cydeo.converter;

import com.cydeo.dto.RoleDto;
import com.cydeo.service.RoleService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RoleDtoConverter implements Converter<String, RoleDto> {

    private final RoleService roleService;

    public RoleDtoConverter(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public RoleDto convert(String id) {

        if (id == null || id.isBlank()) {
            return null;
        }
        return roleService.findById(Long.parseLong(id));
    }
}
