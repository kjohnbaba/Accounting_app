package com.cydeo.entity;

import com.cydeo.entity.common.BaseEntity;
import com.cydeo.enums.ClientVendorType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "clients_vendors")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientVendor extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String clientVendorName;


    @Column(nullable = false)
    private String phone;

    @NotNull
    private String website;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClientVendorType clientVendorType;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

}
