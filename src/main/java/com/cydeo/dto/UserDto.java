package com.cydeo.dto;


import lombok.*;

import javax.validation.constraints.*;

// we exclude lombok setter to be able to validate confirm password field.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;

    @NotBlank(message = "Username is required")
    @Email(message = "Email is required field")
//    @Pattern("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")
//    @Pattern("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@(?!-)[a-zA-Z0-9.-]+$")    // email validation permitted by RFC 5322
    private String username;

    @Setter(AccessLevel.NONE)
    @NotBlank(message = "password is required field")  // @Pattern also checks if it is not blank
    @Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{4,}", message = "password should be at least 4 characters long and contains 1 capital letter, 1 small letter, 1 special character or number")
    private String password;

    @Setter(AccessLevel.NONE)
    @NotNull(message = "Password should match")
    private String confirmPassword;

    @NotBlank(message = "First name is required field")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters long")
    private String firstname;

    @NotBlank(message = "Last name is required field")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters long")
    private String lastname;

    @NotBlank(message = "Phone number is required field") // @Pattern also checks if it is not blank
    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$" // +111 (202) 555-0125  +1 (202) 555-0125
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"                                  // +111 123 456 789
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$", message = "Phone Number is required field and may be in any valid phone number format.") // +111 123 45 67 89
    private String phone;

    @NotNull(message = "Please select a role")
    private RoleDto role;

    @NotNull(message = "Please select a customer")
    private CompanyDto company;

    private boolean isOnlyAdmin;

    public void setPassword(String password) {
        this.password = password;
        checkConfirmPassword();
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        checkConfirmPassword();
    }

    private void checkConfirmPassword() {
        if (password != null && !password.equals(confirmPassword)) {
            this.confirmPassword = null;
        }
    }


}
