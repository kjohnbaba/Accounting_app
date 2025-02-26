package com.cydeo.service.integration_tests;

import com.cydeo.dto.UserDto;
import com.cydeo.entity.User;
import com.cydeo.exception.UserNotFoundException;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.SecuritySetUpUtil;
import com.cydeo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceImpl_IntTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        SecuritySetUpUtil.setUpSecurityContext();
    }

    @Test
    void testFindByUsername() {
        String username = "admin@greentech.com";
        UserDto userDto = userService.findByUsername(username);
        assertNotNull(userDto);
        assertEquals(username, userDto.getUsername());
    }

    @Test
    void testFindByUsername_NotFound() {
        Throwable throwable = catchThrowable(() -> userService.findByUsername("nonexistentuser"));
        assertInstanceOf(UserNotFoundException.class, throwable);
        assertThat(throwable.getMessage()).isEqualTo("User not found");
    }

    @Test
    void testListAllByLoggedInUser() {
        List<UserDto> result = userService.listAllByLoggedInUser();
        assertNotNull(result);
        assertFalse(result.isEmpty());

        UserDto userDto = userService.findByUsername("admin@greentech.com");
        assertThat(result.get(0)).usingRecursiveComparison()
                .ignoringFields("onlyAdmin")
                .isEqualTo(userDto);
    }

    @Test
    void testFindById() {
        Long id = 2L;
        UserDto userDto = userService.findById(id);
        assertNotNull(userDto);
        assertEquals(id, userDto.getId());
    }

    @Test
    void testFindById_NotFound() {
        Throwable throwable = catchThrowable(() -> userService.findById(0L));
        assertInstanceOf(UserNotFoundException.class, throwable);
        assertThat(throwable.getMessage()).isEqualTo("User not found");
    }

    @Test
    void testSave() {
        UserDto userDto = new UserDto();
        userDto.setUsername("newuser@cydeo.com");
        userDto.setPassword("password");

        UserDto savedUserDto = userService.save(userDto);

        assertNotNull(savedUserDto);
        assertEquals("newuser@cydeo.com", savedUserDto.getUsername());
        assertTrue(passwordEncoder.matches("password", savedUserDto.getPassword()));
    }

    @Test
    void testUpdate() {
        UserDto userDto = userService.findById(2L);
        userDto.setUsername("updateduser");
        userDto.setPassword("newpassword");

        UserDto updatedUserDto = userService.update(userDto);

        assertNotNull(updatedUserDto);
        assertEquals("updateduser", updatedUserDto.getUsername());
        assertTrue(passwordEncoder.matches("newpassword", updatedUserDto.getPassword()));
    }

    @Test
    void testDeleteById() {
        Long id = 2L;
        userService.deleteById(id);

        User user = userRepository.findById(id).orElseThrow();
        assertTrue(user.getIsDeleted());
        assertEquals("admin@greentech.com-2", user.getUsername());
    }

    @Test
    void testIsUsernameExist() {
        UserDto userDto = userService.findByUsername("admin2@greentech.com");
        userDto.setUsername("employee@greentech.com");

        boolean result = userService.isUsernameExist(userDto);

        assertTrue(result);
    }

    @Test
    void testIsUsernameExist_NotExist() {
        UserDto userDto = new UserDto();
        userDto.setUsername("nonexistentuser");

        boolean result = userService.isUsernameExist(userDto);

        assertFalse(result);
    }
}

