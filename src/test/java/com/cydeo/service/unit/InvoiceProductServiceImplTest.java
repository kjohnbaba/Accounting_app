package com.cydeo.service.unit;


import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.entity.InvoiceProduct;
import com.cydeo.repository.InvoiceProductRepository;
import com.cydeo.repository.InvoiceRepository;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.SecurityService;
import com.cydeo.service.UserService;
import com.cydeo.service.impl.InvoiceProductServiceImpl;
import com.cydeo.util.MapperUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class InvoiceProductServiceImplTest {

    @Mock
    private InvoiceProductRepository invoiceProductRepository;
    @Mock
    private MapperUtil mapperUtil;
    @Mock
    private SecurityService securityService;
    @Mock
    private InvoiceRepository invoiceRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private InvoiceProductServiceImpl invoiceProductService;


    @Test
    public void findInvoiceProductById() {

    }

    @Test
    public void findTotalCostAndTotalSalesAndTotalProfitAndLoss(){}

    @Test
    public void totalCostOrSale(){}

    @Test
    public void getLast3InvoiceProductAndInvoices() {}

    @Test
    public void saveInvoiceProduct() {}

    @Test
    public void listAllByInvoiceIdAndCalculateTotalPrice() {}

    @Test
    public void getTotalPriceWithTax(){}

    @Test
    public void deleteInvoiceProduct() {}

    @Test
    public void getMonthlyProfitLoss() {}

}
