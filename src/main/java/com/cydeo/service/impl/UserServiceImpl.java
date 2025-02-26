package com.cydeo.service.impl;

import com.cydeo.dto.UserDto;
import com.cydeo.entity.User;
import com.cydeo.enums.CompanyStatus;
import com.cydeo.exception.UserNotFoundException;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.SecurityService;
import com.cydeo.service.UserService;
import com.cydeo.util.MapperUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MapperUtil mapperUtil;
    private final SecurityService securityService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           MapperUtil mapperUtil,
                           @Lazy SecurityService securityService,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mapperUtil = mapperUtil;
        this.securityService = securityService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        return mapperUtil.convert(user, new UserDto());
    }

    @Override
    public List<UserDto> listAllByLoggedInUser() {
        UserDto loggedInUser = securityService.getLoggedInUser();
        List<User> userList;
        if (loggedInUser.getRole().getId() == 1) { // "Root User"
            userList = userRepository.findAllByRole_Id(2); // "Admin"
        } else {
            userList = userRepository.findAllByCompany_Id(loggedInUser.getCompany().getId());
        }
        return userList.stream()
                .sorted(Comparator.comparing((User u) -> u.getCompany().getTitle())
                        .thenComparing(u -> u.getRole().getDescription()))
                .map(entity -> {
                    UserDto dto = mapperUtil.convert(entity, new UserDto());
                    dto.setOnlyAdmin(dto.getRole().getDescription().equals("Admin") && this.checkIfOnlyAdmin(dto));
                    return dto;
                })
                .toList();
    }

    @Override
    public UserDto findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        UserDto dto = mapperUtil.convert(user, new UserDto());
        dto.setOnlyAdmin(dto.getRole().getDescription().equals("Admin") && this.checkIfOnlyAdmin(dto));
        return dto;
    }

    @Override
    public UserDto save(UserDto user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User user1 = mapperUtil.convert(user, new User());
        user1.setEnabled(securityService.getLoggedInUser().getCompany().getCompanyStatus().equals(CompanyStatus.ACTIVE));
        return mapperUtil.convert(userRepository.save(user1), new UserDto());
    }

    @Override
    public UserDto update(UserDto userDto) {
        User saved = userRepository.findById(userDto.getId()).orElseThrow(UserNotFoundException::new);
        User user = mapperUtil.convert(userDto, new User());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(saved.isEnabled());
        return mapperUtil.convert(userRepository.save(user), new UserDto());
    }

    @Override
    public void deleteById(Long id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        user.setIsDeleted(true);
        user.setUsername(user.getUsername() + "-" + user.getId());
        userRepository.save(user);
    }

    @Override
    public boolean isUsernameExist(UserDto userDto) {
        User user = userRepository.findByUsername(userDto.getUsername()).orElse(null);
        if (user == null) return false;
        return !Objects.equals(userDto.getId(), user.getId());
    }

    private boolean checkIfOnlyAdmin(UserDto userDto) {
        int countAdmins = userRepository.countAllByCompany_IdAndRole_Description(userDto.getCompany().getId(), "Admin");
        return countAdmins == 1;
    }
}
