package com.cydeo.service.integration_tests;

import com.cydeo.dto.RoleDto;
import com.cydeo.dto.UserDto;
import com.cydeo.entity.Role;
import com.cydeo.exception.RoleNotFoundException;
import com.cydeo.repository.RoleRepository;
import com.cydeo.service.RoleService;
import com.cydeo.service.SecurityService;
import com.cydeo.service.SecuritySetUpUtil;
import com.cydeo.util.MapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RoleServiceImpl_IntTest {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void testFindById() {
        Role role = new Role();
        role.setId(1L);
        role.setDescription("Admin");
        roleRepository.save(role);

        RoleDto result = roleService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Admin", result.getDescription());
    }

    @Test
    void testFindById_NotFound() {
        Throwable throwable = catchThrowable(() -> roleService.findById(0L));

        assertInstanceOf(RoleNotFoundException.class, throwable);
        assertThat(throwable.getMessage()).isEqualTo("Role not found");
    }

    @Test
    void testListRoleByLoggedInUser_RootUser() {
        SecuritySetUpUtil.setUpSecurityContext(1L, "root@cydeo.com");

        List<RoleDto> result = roleService.listRoleByLoggedInUser();

        assertNotNull(result);
        assertThat(result).hasSize(1);
        assertEquals("Admin", result.get(0).getDescription());
    }

    @Test
    void testListRoleByLoggedInUser_NonRootUser() {
        SecuritySetUpUtil.setUpSecurityContext(2L, "admin@greentech.com");

        List<RoleDto> result = roleService.listRoleByLoggedInUser();

        assertNotNull(result);
        assertThat(result).hasSize(3);
        assertEquals("Admin", result.get(0).getDescription());
    }
}

