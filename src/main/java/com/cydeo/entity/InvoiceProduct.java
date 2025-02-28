package com.cydeo.entity;

import com.cydeo.entity.common.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;


@Entity(name = "invoice_products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

public class InvoiceProduct extends BaseEntity {

    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private Integer tax;

    @Column(name = "profit_loss")
    private BigDecimal profitLoss;

    @Column(name = "remaining_quantity")
    private int remainingQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

}

