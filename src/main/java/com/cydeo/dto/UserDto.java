package com.cydeo.dto;

import com.cydeo.annotations.EmailExist;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


import javax.validation.constraints.*;


@NoArgsConstructor
@AllArgsConstructor
public class UserDto  {

    private Long id;
    @NotBlank(message = "Email is required field.")
    @Email(message="The email is not a valid email.")
    @EmailExist
    private String username;
    @NotBlank(message = "Password is required field")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{4,}$",message = "Password should be at least 4 characters long and needs to contain 1 capital letter, 1 small letter and 1 special character or number.")
    private String password;
    @NotBlank(message = "confirm password is required field")
    private String confirmPassword;
    @NotBlank(message="First name is required field")
    @Size(min=2,max=50,message="Firstname must be between 2 and 50 characters")
    private String firstname;
    @NotBlank(message="Lastname is required field")
    @Size(min=2,max=50,message="Lastname must be between 2 and 50 characters")
    private String lastname;
    @NotBlank(message = "Phone Number is required field and may be in any valid phone number format")
    @Pattern(regexp =  "^(1[ \\-\\+]{0,3}|\\+1[ -\\+]{0,3}|\\+1|\\+)?((\\(\\+?1-[2-9][0-9]{1,2}\\))|(\\(\\+?[2-8][0-9][0-9]\\))|(\\(\\+?[1-9][0-9]\\))|(\\(\\+?[17]\\))|(\\([2-9][2-9]\\))|([ \\-\\.]{0,3}[0-9]{2,4}))?([ \\-\\.][0-9])?([ \\-\\.]{0,3}[0-9]{2,4}){2,3}$")
    private String phone;
    @NotNull(message = "Please select a Role.")
    private RoleDto role;
    @NotNull(message = "Please select a Company.")
    private CompanyDto company;
    private boolean isOnlyAdmin;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        checkConfirmPassWord();
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        checkConfirmPassWord();
    }

    private void checkConfirmPassWord() {
        if(this.password == null || this.confirmPassword == null){
            return;
        }else if(!this.password.equals(confirmPassword)){
            this.confirmPassword = null;
        }
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public RoleDto getRole() {
        return role;
    }

    public void setRole(RoleDto role) {
        this.role = role;
    }

    public CompanyDto getCompany() {
        return company;
    }

    public void setCompany(CompanyDto company) {
        this.company = company;
    }

    public boolean isOnlyAdmin() {
        return isOnlyAdmin;
    }

    public void setOnlyAdmin(boolean onlyAdmin) {
        isOnlyAdmin = onlyAdmin;
    }
}
