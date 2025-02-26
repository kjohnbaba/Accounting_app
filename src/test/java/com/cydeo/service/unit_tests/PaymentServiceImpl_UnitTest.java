package com.cydeo.service.unit_tests;

import com.cydeo.dto.CompanyDto;
import com.cydeo.dto.PaymentDto;
import com.cydeo.dto.common.ChargeRequest;
import com.cydeo.entity.Payment;
import com.cydeo.enums.Months;
import com.cydeo.repository.PaymentRepository;
import com.cydeo.service.CompanyService;
import com.cydeo.service.impl.PaymentServiceImpl;
import com.cydeo.util.MapperUtil;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImpl_UnitTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Spy
    MapperUtil mapperUtil = new MapperUtil(new ModelMapper());

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Value("${stripe_secret_key}")
    private String stripeSecretKey;

    private CompanyDto companyDto;

    @BeforeEach
    public void setUp() {
        // Initialize the companyDto
        companyDto = new CompanyDto();
        companyDto.setId(1L);

        // Set the stripeSecretKey value
        ReflectionTestUtils.setField(paymentService, "stripeSecretKey", "sk_test_4eC39HqLyjWDarjtT1zdp7dc");
    }

    @Test
    void testGetAllPaymentsByYear_PaymentsExist() {
        int year = 2022;
        List<Payment> payments = List.of(new Payment(), new Payment());
        payments.get(0).setMonth(Months.JAN);
        payments.get(1).setMonth(Months.FEB);

        when(companyService.getCompanyDtoByLoggedInUser()).thenReturn(companyDto);
        when(paymentRepository.findAllByYearAndCompany_Id(year, companyDto.getId())).thenReturn(payments);

        List<PaymentDto> result = paymentService.getAllPaymentsByYear(year);

        assertThat(result).hasSize(2);
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void testGetAllPaymentsByYear_NoPaymentsExist() {
        int year = 2022;
        List<Payment> payments = Collections.emptyList();

        when(companyService.getCompanyDtoByLoggedInUser()).thenReturn(companyDto);
        when(paymentRepository.findAllByYearAndCompany_Id(year, companyDto.getId())).thenReturn(payments);

        paymentService.getAllPaymentsByYear(year);

        verify(paymentRepository, times(12)).save(any(Payment.class));
    }

    @Test
    void testGetPaymentById_Found() {
        Long id = 1L;
        Payment payment = new Payment();
        payment.setPaymentDate(LocalDate.now());
        payment.setAmount(BigDecimal.TEN);
        PaymentDto paymentDto = mapperUtil.convert(payment, new PaymentDto());

        when(paymentRepository.findById(id)).thenReturn(Optional.of(payment));

        PaymentDto result = paymentService.getPaymentById(id);

        assertThat(result).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(paymentDto);
    }

    @Test
    void testGetPaymentById_NotFound() {
        Long id = 1L;

        when(paymentRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.getPaymentById(id))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Subscription with this month does not exist.");
    }

    @Test
    void testCharge_Success() throws StripeException {
        Long id = 1L;
        ChargeRequest chargeRequest = new ChargeRequest();
        chargeRequest.setStripeToken("token");
        Payment payment = new Payment();
        payment.setId(id);
        payment.setYear(2022);
        payment.setMonth(Months.JAN);
        payment.setAmount(new BigDecimal("250.00"));

        PaymentDto paymentDto = mapperUtil.convert(payment, new PaymentDto());
        paymentDto.setPaid(true);

        Charge charge = new Charge();
        charge.setId("ch_1J2Y3a4b5c6d7e8f9g0h");
        charge.setStatus("succeeded");
        charge.setDescription("Cydeo accounting subscription fee for : 2022 1");

        when(paymentRepository.findById(id)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        try (MockedStatic<Charge> mockedStatic = mockStatic(Charge.class)) {
            mockedStatic.when(() -> Charge.create(any(Map.class))).thenReturn(charge);

            PaymentDto result = paymentService.charge(chargeRequest, id);

            assertThat(result).usingRecursiveComparison()
                    .ignoringExpectedNullFields()
                    .isEqualTo(paymentDto);
            assertThat(result.getDescription()).isEqualTo("Cydeo accounting subscription fee for : 2022 1");
        }
    }

    @Test
    void testCharge_Failure() throws StripeException {
        Long id = 1L;
        ChargeRequest chargeRequest = new ChargeRequest();
        chargeRequest.setStripeToken("token");
        Payment payment = new Payment();
        payment.setId(id);
        payment.setYear(2022);
        payment.setMonth(Months.JAN);
        payment.setAmount(new BigDecimal("250.00"));

        Charge charge = new Charge();
        charge.setId("ch_1J2Y3a4b5c6d7e8f9g0h");
        charge.setStatus("failed");

        when(paymentRepository.findById(id)).thenReturn(Optional.of(payment));

        try (MockedStatic<Charge> mockedStatic = mockStatic(Charge.class)) {
            mockedStatic.when(() -> Charge.create(any(Map.class))).thenReturn(charge);


            assertThatThrownBy(() -> paymentService.charge(chargeRequest, id))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Transaction failed. Please try again!");
        }
    }
}

