package com.cydeo.converter;

import com.cydeo.dto.UserDto;
import com.cydeo.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;

public class UserDtoConverter implements Converter<String, UserDto> {

    private final UserService userService;

    public UserDtoConverter(@Lazy UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDto convert(String source) {
        if(source.isEmpty()) return null;
        return userService.findById(Long.parseLong(source));
    }
}
