package com.cydeo.entity;

import com.cydeo.entity.common.BaseEntity;
import com.cydeo.enums.Months;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name="payments")
@Where(clause = "is_deleted=false")
public class Payment extends BaseEntity implements Serializable {

    private int year;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private boolean isPaid;
    private String companyStripeId;

    @Enumerated(EnumType.STRING)
    private Months month;

    @ManyToOne
    private Company company;


}