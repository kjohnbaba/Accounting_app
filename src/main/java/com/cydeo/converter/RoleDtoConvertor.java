package com.cydeo.converter;

import com.cydeo.dto.RoleDto;
import com.cydeo.service.RoleService;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RoleDtoConvertor implements Converter<String,RoleDto> {

    private final RoleService roleService;

    public RoleDtoConvertor(@Lazy RoleService roleService) {
        this.roleService = roleService;
    }


    @Override
    public RoleDto convert(String source) {
        if(source.isEmpty()) return null;
        return roleService.findById(Long.parseLong(source));
    }
}
