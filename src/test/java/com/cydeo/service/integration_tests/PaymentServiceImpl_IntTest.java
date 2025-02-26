package com.cydeo.service.integration_tests;

import com.cydeo.dto.CompanyDto;
import com.cydeo.dto.PaymentDto;
import com.cydeo.dto.common.ChargeRequest;
import com.cydeo.entity.Company;
import com.cydeo.entity.Payment;
import com.cydeo.enums.Months;
import com.cydeo.repository.PaymentRepository;
import com.cydeo.service.CompanyService;
import com.cydeo.service.SecuritySetUpUtil;
import com.cydeo.service.impl.PaymentServiceImpl;
import com.cydeo.util.MapperUtil;
import com.stripe.exception.*;
import com.stripe.model.Charge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Transactional
@Disabled
class PaymentServiceImpl_IntTest {

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Spy
    private MapperUtil mapperUtil = new MapperUtil(new ModelMapper());

    @Captor
    private ArgumentCaptor<Payment> paymentArgumentCaptor;

    @Value("${stripe_secret_key}")
    private String stripeSecretKey;

    private CompanyDto companyDto;
    private Company company;
    private Payment payment;

    @BeforeEach
    void setUp() {
        companyDto = new CompanyDto();
        companyDto.setId(1L);

        company = new Company();
        company.setId(1L);

        payment = new Payment();
        payment.setId(1L);
        payment.setMonth(Months.JAN);
        payment.setYear(2023);
        payment.setPaid(false);
        payment.setAmount(BigDecimal.valueOf(250));
        payment.setCompany(company);
    }

    @Test
//    @WithMockUser(username = "admin@greentech.com", password = "Abc1", roles = "ADMIN")
    void testGetAllPaymentsByYear_NoPayments() {
        SecuritySetUpUtil.setUpSecurityContext(2L, "admin@greentech.com");
        when(paymentRepository.findAllByYearAndCompany_Id(2023, companyDto.getId())).thenReturn(List.of());

        List<PaymentDto> result = paymentService.getAllPaymentsByYear(2023);

        verify(paymentRepository, times(12)).save(paymentArgumentCaptor.capture());
        List<Payment> savedPayments = paymentArgumentCaptor.getAllValues();

        assertThat(savedPayments).hasSize(12);
        assertThat(result).hasSize(12);
    }

    @Test
    void testGetPaymentById() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        PaymentDto result = paymentService.getPaymentById(1L);

        assertNotNull(result);
    }

    @Test
    void testGetPaymentById_NotFound() {
        Throwable throwable = catchThrowable(() -> paymentService.getPaymentById(1L));

        assertInstanceOf(NoSuchElementException.class, throwable);
        assertThat(throwable.getMessage()).isEqualTo("Subscription with this month does not exist.");
    }

    @Test
    void testCharge_Successful() throws StripeException {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        ChargeRequest chargeRequest = new ChargeRequest();
        chargeRequest.setStripeToken("dummy_token");

        Charge charge = mock(Charge.class);
        when(charge.getStatus()).thenReturn("succeeded");
        when(charge.getId()).thenReturn("ch_123");
        when(charge.getDescription()).thenReturn("description");

        mockStatic(Charge.class);
        when(Charge.create(anyMap())).thenReturn(charge);

        PaymentDto result = paymentService.charge(chargeRequest, 1L);

        assertNotNull(result);
        verify(paymentRepository).save(paymentArgumentCaptor.capture());
        Payment savedPayment = paymentArgumentCaptor.getValue();

        assertTrue(savedPayment.isPaid());
        assertEquals("ch_123", savedPayment.getCompanyStripeId());
    }

    @Test
    void testCharge_Failure() throws StripeException {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        ChargeRequest chargeRequest = new ChargeRequest();
        chargeRequest.setStripeToken("dummy_token");

        Charge charge = mock(Charge.class);
        when(charge.getStatus()).thenReturn("failed");

        mockStatic(Charge.class);
        when(Charge.create(anyMap())).thenReturn(charge);

        Throwable throwable = catchThrowable(() -> paymentService.charge(chargeRequest, 1L));

        assertInstanceOf(IllegalArgumentException.class, throwable);
        assertThat(throwable.getMessage()).isEqualTo("Transaction failed. Please try again!");
    }
}

