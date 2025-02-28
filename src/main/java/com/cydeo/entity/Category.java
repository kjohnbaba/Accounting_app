package com.cydeo.entity;

import com.cydeo.entity.common.BaseEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "categories",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"description", "company_id"})})
@NoArgsConstructor
public class Category extends BaseEntity {

    @NotBlank(message = "Description is a required field.")
    @Size(min = 2, max = 100, message = "Description should have 2-100 characters long.")
    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;
}
