package com.cydeo.entity;

import com.cydeo.entity.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity(name = "users")
@Table(name = "users")
@Getter
@Setter
public class User extends BaseEntity {

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Username is mandatory")
    @Size(min = 3, max = 100, message = "Username must be between 3 and 100 characters")
    private String username;
    
    // Can change username length later on, currently temporary 
  
    @Column(nullable = false)
    @NotBlank(message = "Password is mandatory")
    private String password;

    @Column(nullable = false)
    @NotBlank(message = "First name is mandatory")
    private String firstname;

    @Column(nullable = false)
    @NotBlank(message = "Last name is mandatory")
    private String lastname;

    @Column(nullable = false)
    @NotBlank(message = "Phone is mandatory")
    private String phone;

    @Column(nullable = false)
    private boolean enabled;

    //have to test these later

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    @NotNull(message = "Role is mandatory")
    private Role role;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id", nullable = false)
    @NotNull(message = "Company is mandatory")
    private Company company;


}
