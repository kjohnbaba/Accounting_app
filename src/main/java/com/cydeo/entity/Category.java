package com.cydeo.entity;

import com.cydeo.entity.common.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "categories")
@Where(clause = "is_deleted=false")
public class Category extends BaseEntity {

    private String description;

    @ManyToOne
    private Company company;

    @OneToMany(mappedBy = "category")
    private List<Product> product;

}
