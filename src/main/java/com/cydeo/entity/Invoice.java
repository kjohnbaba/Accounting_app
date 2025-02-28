package com.cydeo.entity;

import com.cydeo.entity.common.BaseEntity;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

@Table(name = "invoices")
public class Invoice extends BaseEntity {

    @Column(nullable = false)
    private String invoiceNo;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InvoiceStatus invoiceStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InvoiceType invoiceType;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne()
    @JoinColumn(name = "client_vendor_id", nullable = false)
    private ClientVendor clientVendor;

    @ManyToOne()
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(nullable = false)
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<InvoiceProduct> invoiceProducts; // it must be list , otherwise it won't save them. - 'greg'
}
