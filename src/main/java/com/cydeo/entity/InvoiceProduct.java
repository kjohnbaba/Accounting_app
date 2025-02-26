package com.cydeo.entity;

import com.cydeo.entity.common.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "invoice_products")
@Where(clause = "is_deleted=false")
public class InvoiceProduct extends BaseEntity {

    private Integer quantity;
    private BigDecimal price;
    private Integer tax;
    private BigDecimal profitLoss;
    private int remainingQuantity;

    @ManyToOne
    private Invoice invoice;

    @ManyToOne
    private Product product;

}
