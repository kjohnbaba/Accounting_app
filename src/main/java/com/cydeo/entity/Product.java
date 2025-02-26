package com.cydeo.entity;


import com.cydeo.entity.common.BaseEntity;
import com.cydeo.enums.ProductUnit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Where(clause = "is_deleted=false")
@Table(name = "products")
public class Product extends BaseEntity {

    private String name;
    private int quantityInStock;
    private int lowLimitAlert;

    @Enumerated(EnumType.STRING)
    private ProductUnit productUnit;

    @ManyToOne
    private Category category;

    @OneToMany(mappedBy = "product")
    private List<InvoiceProduct> invoiceProducts;

}
