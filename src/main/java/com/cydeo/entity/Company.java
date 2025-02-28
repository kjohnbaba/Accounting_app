package com.cydeo.entity;

import com.cydeo.entity.common.BaseEntity;
import com.cydeo.enums.CompanyStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "companies", uniqueConstraints = {@UniqueConstraint(columnNames = {"title"})})
@Getter
@Setter
public class Company extends BaseEntity {

    @NotBlank(message = "Title is a required field.")
    @Size(min = 2, max = 100, message = "Title should be 2-100 characters long.")
    @Column(nullable = false, unique = true)
    private String title;

    @NotBlank(message = "Phone Number is a required field and may be in any valid phone number format.")
    @Pattern(regexp = "^\\+?[0-9\\s\\(\\)\\-]{10,20}$", message = "Phone Number is required and should be in a valid format (e.g., +1 (957) 463-7174).")
    @Column(nullable = false)
    private String phone;

    @NotBlank(message = "Website is required")
    @Column(nullable = false)
    private String website;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CompanyStatus companyStatus;

    @Valid
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;
}
