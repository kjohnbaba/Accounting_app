package com.cydeo.service.unit_tests;

import com.cydeo.dto.RoleDto;
import com.cydeo.dto.UserDto;
import com.cydeo.entity.Role;
import com.cydeo.exception.RoleNotFoundException;
import com.cydeo.repository.RoleRepository;
import com.cydeo.service.SecurityService;
import com.cydeo.service.impl.RoleServiceImpl;
import com.cydeo.util.MapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceImpl_UnitTest {

    @InjectMocks
    private RoleServiceImpl roleService;

    @Mock
    private RoleRepository roleRepository;

    @Spy
    private MapperUtil mapperUtil = new MapperUtil(new ModelMapper());

    @Mock
    private SecurityService securityService;

    private Role role;
    private RoleDto roleDto;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1L);
        role.setDescription("Admin");

        roleDto = new RoleDto();
        roleDto.setId(1L);
        roleDto.setDescription("Admin");

        userDto = new UserDto();
        userDto.setRole(roleDto);
    }

    @Test
    void testFindById_WhenRoleExists() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        RoleDto result = roleService.findById(1L);

        assertThat(result.getDescription()).isEqualTo("Admin");
        verify(roleRepository).findById(1L);
    }

    @Test
    void testFindById_WhenRoleDoesNotExist() {
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roleService.findById(1L)).isInstanceOf(RoleNotFoundException.class);
        verify(roleRepository).findById(1L);
    }

    @Test
    void testListRoleByLoggedInUser_WhenLoggedInUserIsRoot() {
        userDto.getRole().setDescription("Root User");
        when(securityService.getLoggedInUser()).thenReturn(userDto);
        when(roleRepository.findByDescription("Admin")).thenReturn(role);

        List<RoleDto> result = roleService.listRoleByLoggedInUser();

        assertThat(result.get(0).getDescription()).isEqualTo("Admin");
        verify(securityService).getLoggedInUser();
        verify(roleRepository).findByDescription("Admin");
    }

    @Test
    void testListRoleByLoggedInUser_WhenLoggedInUserIsNotRoot() {
        Role manager = new Role();
        manager.setDescription("Manager");
        userDto.getRole().setDescription("Admin");
        when(securityService.getLoggedInUser()).thenReturn(userDto);
        when(roleRepository.findAll()).thenReturn(List.of(role, manager));

        List<RoleDto> result = roleService.listRoleByLoggedInUser();

        assertThat(result).hasSize(2);
        verify(securityService).getLoggedInUser();
        verify(roleRepository).findAll();
    }

    @Test
    void testListRoleByLoggedInUser_FilterOutRootUserRole() {
        userDto.getRole().setDescription("Admin");
        Role rootRole = new Role();
        rootRole.setId(2L);
        rootRole.setDescription("Root User");
        when(securityService.getLoggedInUser()).thenReturn(userDto);
        when(roleRepository.findAll()).thenReturn(List.of(role, rootRole));

        List<RoleDto> result = roleService.listRoleByLoggedInUser();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDescription()).isEqualTo("Admin");
        verify(securityService).getLoggedInUser();
        verify(roleRepository).findAll();
    }
}

