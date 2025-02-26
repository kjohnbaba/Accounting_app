package com.cydeo.service.unit_tests;

import com.cydeo.TestDocumentInitializer;
import com.cydeo.dto.UserDto;
import com.cydeo.entity.User;
import com.cydeo.exception.UserNotFoundException;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.SecurityService;
import com.cydeo.service.impl.UserServiceImpl;
import com.cydeo.util.MapperUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImpl_UnitTest {

    @Mock
    UserRepository userRepository;

    @Mock
    SecurityService securityService;

    @Spy
    MapperUtil mapperUtil = new MapperUtil(new ModelMapper());

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void should_find_user_by_username_return_user_if_exists() {
        // given
        UserDto userDto = TestDocumentInitializer.getUser("Manager");
        User user = mapperUtil.convert(userDto, new User());
        // when
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        UserDto actualUser = userService.findByUsername(userDto.getUsername());
        // then
        assertThat(actualUser).usingRecursiveComparison()
                .ignoringFields("password", "confirmPassword")
                .isEqualTo(userDto);
    }

    @Test
    void find_by_username_should_throw_exception_if_user_does_exist() {
        // when
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        Throwable throwable = catchThrowable(() -> userService.findByUsername(anyString()));
        // then
        assertInstanceOf(UserNotFoundException.class, throwable);
        assertEquals("User not found", throwable.getMessage());
    }

    @Test
    void should_return_sorted_user_list_for_root_user() {
        // given
        List<UserDto> userDtos = Arrays.asList(
                TestDocumentInitializer.getUser("Root User"),
                TestDocumentInitializer.getUser("Admin"),
                TestDocumentInitializer.getUser("Admin"),
                TestDocumentInitializer.getUser("Admin"));
        userDtos.get(0).getCompany().setTitle("Zet");
        userDtos.get(1).getCompany().setTitle("Etc");
        userDtos.get(2).getCompany().setTitle("Abc");
        userDtos.get(3).getCompany().setTitle("Ower");
        List<User> userList = userDtos.stream()
                .map(userDto -> mapperUtil.convert(userDto, new User()))
                .toList();
        List<UserDto> expectedList = userDtos.stream()
                .sorted(Comparator.comparing((UserDto u) -> u.getCompany().getTitle())
                        .thenComparing(u -> u.getRole().getDescription()))
                .toList();

        // when
        when(securityService.getLoggedInUser()).thenReturn(userDtos.get(0));
        when(userRepository.findAllByRole_Id(anyLong())).thenReturn(userList);

        List<UserDto> actualList = userService.listAllByLoggedInUser();

        // then
        Assertions.assertThat(userList).hasSize(4);
        assertThat(actualList).usingRecursiveComparison()
                .ignoringFields("password", "confirmPassword")
                .isEqualTo(expectedList);
    }

    @Test
    void should_return_sorted_user_list_for_admin() {
        // given
        List<UserDto> userDtos = Arrays.asList(
                TestDocumentInitializer.getUser("Admin"),
                TestDocumentInitializer.getUser("Admin"),
                TestDocumentInitializer.getUser("Manager"),
                TestDocumentInitializer.getUser("Employee"));
        userDtos.get(0).getCompany().setTitle("Zet");
        userDtos.get(0).setOnlyAdmin(true);
        userDtos.get(0).setId(2L);
        userDtos.get(1).getCompany().setTitle("Etc");
        userDtos.get(1).setOnlyAdmin(false);
        userDtos.get(2).getCompany().setTitle("Abc");
        userDtos.get(3).getCompany().setTitle("Ower");
        List<User> userList = userDtos.stream()
                .map(userDto -> mapperUtil.convert(userDto, new User()))
                .toList();
        List<UserDto> expectedList = userDtos.stream()
                .sorted(Comparator.comparing((UserDto u) -> u.getCompany().getTitle())
                        .thenComparing(u -> u.getRole().getDescription()))
                .toList();

        // when
        when(securityService.getLoggedInUser()).thenReturn(userDtos.get(0));
        when(userRepository.findAllByRole_Id(anyLong())).thenReturn(userList);
        when(userRepository.countAllByCompany_IdAndRole_Description(anyLong(), anyString())).thenReturn(2, 1, 1, 1);

        List<UserDto> actualList = userService.listAllByLoggedInUser();
        actualList.forEach(userDto -> System.out.println(userDto.isOnlyAdmin()));

        // then
        Assertions.assertThat(userList).hasSize(4);
        assertThat(actualList.get(1).isOnlyAdmin()).isFalse();
        assertThat(actualList).usingRecursiveComparison()
                .ignoringFields("password", "confirmPassword")
                .isEqualTo(expectedList);
    }

    @Test
    void should_find_user_by_id_return_user_if_exists() {
        // given
        UserDto userDto = TestDocumentInitializer.getUser("Admin");
        User user = mapperUtil.convert(userDto, new User());
        // when
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(user));
        when(userRepository.countAllByCompany_IdAndRole_Description(anyLong(), anyString())).thenReturn(1);
//        when(mapperUtil.convert(any(User.class), any(UserDto.class))).thenReturn(userDto);
//        when(mapperUtil.convert(any(User.class), ArgumentMatchers.<Class<UserDto>>any())).thenReturn(UserDto.class);
//        when(mapperUtil.convert(user, any(UserDto.class))).thenReturn(userDto); // exceptions.misusing.InvalidUseOfMatchersException
//        when(mapperUtil.convert(user, new UserDto())).thenReturn(userDto); // mockito.exceptions.misusing.PotentialStubbingProblem
//        when(userMapper.convertToDto(user)).thenReturn(userDto);

        UserDto actualUser = userService.findById(1L);

        // then
        assertThat(actualUser.isOnlyAdmin()).isTrue();
        assertThat(actualUser).usingRecursiveComparison()
                .ignoringFields("password", "confirmPassword", "isOnlyAdmin")
                .isEqualTo(userDto);
    }

    @Test
    void find_by_id_should_throw_exception_if_user_does_exist() {
        // when
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        // it throws exception since no mock of userRepository and userRepository.findById(1L) returns null
        Throwable throwable = catchThrowable(() -> userService.findById(1L));
        // assertThrows also works, but we can also assert message with catchThrowable
        // assertThrows(AccountingException.class, ()-> userService.findUserById(1L));

        // then
        assertThat(throwable).isInstanceOf(UserNotFoundException.class);
        // or
        assertInstanceOf(UserNotFoundException.class, throwable);

        assertEquals("User not found", throwable.getMessage());
    }

    @Test
    void should_save_user_and_return_saved_user() {
        // given
        UserDto userDto = TestDocumentInitializer.getUser("Manager");
        User user = mapperUtil.convert(userDto, new User());

        // when
        when(passwordEncoder.encode(anyString())).thenReturn(userDto.getPassword());
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(securityService.getLoggedInUser()).thenReturn(userDto);

        UserDto savedUser = userService.save(userDto);

        // then
        assertThat(savedUser).isNotNull();
                verify(passwordEncoder).encode(anyString());
        assertThat(savedUser).usingRecursiveComparison()
                .ignoringFields("password", "confirmPassword")
                .isEqualTo(userDto);
    }

    @Test
    void should_update_user_and_return_updated_user() {
        // given
        UserDto userDto = TestDocumentInitializer.getUser("Admin");
        userDto.setUsername("JohnSmith@test.com");
        userDto.setFirstname("John");
        User user = mapperUtil.convert(userDto, new User());

        // when
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(any(User.class))).thenReturn(user);


        UserDto updatedUser = userService.update(userDto);

        // then
        verify(passwordEncoder).encode(anyString());
        assertThat(updatedUser).usingRecursiveComparison()
                .ignoringFields("password", "confirmPassword")
                .isEqualTo(userDto);
    }

    @Test
    void should_soft_delete_user() {
        // given
        UserDto userDto = TestDocumentInitializer.getUser("Manager");
        User user = mapperUtil.convert(userDto, new User());
        user.setIsDeleted(false);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteById(user.getId());

        verify(userRepository, times(1)).save(user);
        assertThat(user.getIsDeleted()).isTrue();
        assertNotEquals(userDto.getUsername(), user.getUsername());
    }

    @Test
    void should_throw_exception_when_user_id_to_delete_does_not_exist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> userService.deleteById(1L));
        assertThat(throwable).isInstanceOf(UserNotFoundException.class);
        assertEquals("User not found", throwable.getMessage());
    }


    @ParameterizedTest
    @MethodSource(value = "email")
    void should_return_true_if_username_exist(UserDto userDto, User user, boolean expected) {
        // when
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.ofNullable(user));
        // then
        assertEquals(expected, userService.isUsernameExist(userDto));
    }

    static Stream<Arguments> email(){
        // given
        UserDto userDto = TestDocumentInitializer.getUser("Admin");
        User user = new MapperUtil(new ModelMapper()).convert(userDto, new User());
        user.setId(2L);
        return Stream.of(
                arguments(userDto, user, true),
                arguments(userDto, null, false)
        );
    }

}