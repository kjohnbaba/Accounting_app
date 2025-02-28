package com.cydeo.controller;


import com.cydeo.dto.UserDto;
import com.cydeo.service.CompanyService;
import com.cydeo.service.RoleService;
import com.cydeo.service.SecurityService;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/users")
public class UserController {

    private final RoleService roleService;
    private final CompanyService companyService;
    private final UserService userService;
    private final SecurityService securityService;

    public UserController(RoleService roleService, CompanyService companyService, UserService userService, SecurityService securityService) {
        this.roleService = roleService;
        this.companyService = companyService;
        this.userService = userService;
        this.securityService = securityService;
    }

    @GetMapping("/create")
    public String  createUser(Model model){

        model.addAttribute("newUser",new UserDto());
        model.addAttribute("userRoles",roleService.findAllRole());
        model.addAttribute("companies",companyService.getAllCompanies());
        return "/user/user-create";
    }

    @PostMapping("/create")
    public String postUser(@Validated @ModelAttribute("newUser")  UserDto newUser, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("userRoles",roleService.findAllRole());
            model.addAttribute("companies",companyService.getAllCompanies());
           // model.addAttribute("errors", bindingResult.getAllErrors());
          return "/user/user-create";
        }
        userService.save(newUser);
        return "redirect:/users/list";
    }

    @GetMapping("/list")
    public String ListAllUsers(Model model){
        List<UserDto> users = userService.listAllUsers();
        for(UserDto user : users){
            user.setOnlyAdmin(userService.getNumberOfAdmins(user.getCompany()) == 1 && Objects.equals(user.getRole().getDescription(), "Admin"));
        }
        model.addAttribute("users", users);
        return "/user/user-list";
    }

    @GetMapping("/update/{id}")
    public String editUser(@PathVariable("id") Long id,Model model){

        model.addAttribute("user",userService.findById(id));
        model.addAttribute("userRoles",roleService.findAllRole());
        model.addAttribute("companies",companyService.getAllCompanies());
        return "/user/user-update";
    }
    @PostMapping("/update/{id}")
    public String updateUser(@Valid @ModelAttribute("user")UserDto user, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            model.addAttribute("userRoles",roleService.findAllRole());
            model.addAttribute("companies",companyService.getAllCompanies());
            return "/user/user-update";
        }
        userService.update(user);
        return "redirect:/users/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id){
        String roleDescription = securityService.getLoggedInUser().getRole().getDescription();
        if (Objects.equals(roleDescription, "Admin") || Objects.equals(roleDescription, "Root User")){
            userService.deleteById(id);
        }
        return "redirect:/users/list";
    }
}
