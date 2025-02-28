package com.cydeo.service.impl;

import com.cydeo.dto.CompanyDto;
import com.cydeo.dto.UserDto;
import com.cydeo.entity.User;
import com.cydeo.exceptions.UserNotFoundException;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.SecurityService;
import com.cydeo.service.UserService;
import com.cydeo.util.MapperUtil;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final MapperUtil mapperUtil;
    private final SecurityService securityService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, MapperUtil mapperUtil, @Lazy SecurityService securityService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mapperUtil = mapperUtil;
        this.securityService = securityService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        return mapperUtil.convert(user, new UserDto());
    }

    @Override
    public void save(UserDto userDto) {
        User user = mapperUtil.convert(userDto, new User());
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void update(UserDto userDto) {

        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        User convertedUser = mapperUtil.convert(userDto, new User());
        // convertedUser.setUsername(convertedUser.getUsername());
        convertedUser.setId(user.getId());
        userRepository.save(convertedUser);
    }

    @Override
    public void deleteById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            user.orElseThrow().setUsername(hashEmail(user.get().getUsername()));
            user.orElseThrow().setIsDeleted(true);
            userRepository.save(user.get());
        }
    }


//        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
//        userRepository.delete(user);

    private String hashEmail(String email){
        return "##"+email+"##";

    }

    @Override
    public UserDto findById(Long id) {
        User user = userRepository.findById(id).get();
        return mapperUtil.convert(user, new UserDto());
    }

    @Override
    public List<UserDto> listAllUsers() {
        String loggedInUserRole = securityService.getLoggedInUser().getRole().getDescription();

        if (loggedInUserRole.equals("Root User")) {

            return userRepository.findAllByIsDeletedFalse().stream().filter(user -> user.getRole().getDescription().equals("Admin"))
                    .sorted(Comparator.comparing(user -> user.getCompany().getTitle()))
                    .map(user -> mapperUtil.convert(user, new UserDto())).collect(Collectors.toList());

        } else{

            Long companyId = securityService.getLoggedInUser().getCompany().getId();

            return userRepository.findAllByCompany_IdAndIsDeletedFalse(companyId).stream()
                    .sorted(Comparator.comparing(user -> user.getRole().getDescription()))
                    .map(user -> mapperUtil.convert(user, new UserDto())).collect(Collectors.toList());
        }
    }


    @Override
    public Integer getNumberOfAdmins(CompanyDto companyDto) {
        List<User> allAdminUsersInCompany = userRepository.findAllByCompany_IdAndRole_IdAndIsDeletedFalse(companyDto.getId(), 2L);

        return allAdminUsersInCompany.size();
    }

}
