package com.cydeo.controller;

import com.cydeo.dto.UserDto;
import com.cydeo.service.CompanyService;
import com.cydeo.service.RoleService;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final CompanyService companyService;

    public UserController(UserService userService, RoleService roleService, CompanyService companyService) {
        this.userService = userService;
        this.roleService = roleService;
        this.companyService = companyService;
    }

    @GetMapping("/list")
    public String getUsers(Model model) {
        model.addAttribute("users", userService.listAllByLoggedInUser());
        return "user/user-list";
    }

    @GetMapping("/create")
    public String createUser(Model model) {
        model.addAttribute("newUser", new UserDto());
        return "user/user-create";
    }

    @PostMapping("/create")
    public String insertUser(@Valid @ModelAttribute("newUser") UserDto user, BindingResult bindingResult, Model model) {

        boolean isUsernameExist = userService.isUsernameExist(user);
        if (isUsernameExist) {
            bindingResult.rejectValue("username", " ", "A user with this email already exists. Please try with different email.");
        }
        if (bindingResult.hasErrors()) {
            return "user/user-create";
        }
        userService.save(user);
        return "redirect:/users/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/users/list";
    }

    @GetMapping("/update/{id}")
    public String editUser(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        return "user/user-update";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@Valid @ModelAttribute("user") UserDto user, BindingResult bindingResult) {
        // if path variable name and object field name matches, spring assigns id to user.id automatically
//        user.setId(id);
        boolean isUsernameExist = userService.isUsernameExist(user);
        if (isUsernameExist) {
            bindingResult.rejectValue("username", " ", "A user with this email already exists. Please try with different email.");
        }
        if (bindingResult.hasErrors()) {
            return "user/user-update";
        }
        userService.update(user);
        return "redirect:/users/list";
    }

    @ModelAttribute
    public void commonAttributes(Model model) {
        model.addAttribute("userRoles", roleService.listRoleByLoggedInUser());
        model.addAttribute("companies", companyService.listCompaniesByLoggedInUser());
        model.addAttribute("title", "Cydeo Accounting-User");
    }
}
