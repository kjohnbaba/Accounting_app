package com.cydeo.service.impl;

import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.dto.UserDto;
import com.cydeo.entity.Invoice;
import com.cydeo.entity.InvoiceProduct;
import com.cydeo.entity.User;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.exceptions.InvoiceProductNotFoundException;
import com.cydeo.exceptions.UserNotFoundException;
import com.cydeo.repository.InvoiceProductRepository;
import com.cydeo.repository.InvoiceRepository;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.InvoiceProductService;
import com.cydeo.service.SecurityService;
import com.cydeo.service.UserService;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class InvoiceProductServiceImpl implements InvoiceProductService {

    private final InvoiceProductRepository invoiceProductRepository;
    private final MapperUtil mapperUtil;
    private final UserRepository userRepository;
    private final SecurityService securityService;
    private final InvoiceRepository invoiceRepository;
    private final UserService userService;

    public InvoiceProductServiceImpl(InvoiceProductRepository invoiceProductRepository, MapperUtil mapperUtil, UserRepository userRepository, SecurityService securityService, InvoiceRepository invoiceRepository, UserService userService) {
        this.invoiceProductRepository = invoiceProductRepository;
        this.mapperUtil = mapperUtil;
        this.userRepository = userRepository;
        this.securityService = securityService;
        this.invoiceRepository = invoiceRepository;
        this.userService = userService;
    }

    @Override
    public InvoiceProductDto findInvoiceProductById(Long id) {
        InvoiceProduct item = invoiceProductRepository.findById(id).orElseThrow( ()-> new InvoiceProductNotFoundException("InvoiceProduct not found"));
        return mapperUtil.convert(item, new InvoiceProductDto());
    }

    @Override
    public Map<String, BigDecimal> findTotalCostAndTotalSalesAndTotalProfitAndLoss() {
        UserDto userDto = securityService.getLoggedInUser();
        userService.findByUsername(userDto.getUsername());
        User user = mapperUtil.convert(userService.findByUsername(userDto.getUsername()), new User());
//        User user = userRepository.findByUsername(userDto.getUsername()).get();

        List<InvoiceProduct> listAllApprovedPurchaseInvoiceProduct = invoiceProductRepository.listAllApprovedPurchaseInvoiceProduct(user.getCompany().getId());
        List<InvoiceProduct> listAllSalePurchaseInvoiceProduct = invoiceProductRepository.listAllApprovedSaleInvoiceProduct(user.getCompany().getId());

        BigDecimal profitLoss = listAllSalePurchaseInvoiceProduct.stream().map(InvoiceProduct::getProfitLoss).reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> costs = new HashMap<>();
        costs.put("totalCost", totalCostOrSale(listAllApprovedPurchaseInvoiceProduct));
        costs.put("totalSales", totalCostOrSale(listAllSalePurchaseInvoiceProduct));
        costs.put("profitLoss", profitLoss);

        return costs;
    }

    private static BigDecimal totalCostOrSale(List<InvoiceProduct> invoiceProductList) {

        BigDecimal total = new BigDecimal(0);
        for (InvoiceProduct each : invoiceProductList) {
            BigDecimal priceQuantity = each.getPrice().multiply(BigDecimal.valueOf(each.getQuantity()));
            BigDecimal tax = (each.getPrice().divide(BigDecimal.valueOf(each.getTax())).multiply(BigDecimal.valueOf(each.getQuantity())));
            total = total.add(priceQuantity).add(tax);
        }
        return total;
    }

    @Override
    public List<InvoiceProductDto> getLast3InvoiceProductAndInvoices() {
        UserDto userDto = securityService.getLoggedInUser();
        User user = userRepository.findByUsername(userDto.getUsername()).orElseThrow(()-> new UserNotFoundException("User not found"));
        List<InvoiceProduct> list = new ArrayList<>();
        List<Invoice> last3Invoices = invoiceRepository.getAllByInvoiceStatusAndCompanyOrderByDateDesc(InvoiceStatus.APPROVED, user.getCompany());
        if (last3Invoices.size() == 0) {
            return Arrays.asList(new InvoiceProductDto());
        } else if (last3Invoices.size() == 1) {
            List<InvoiceProduct> latestProduct_1 = invoiceProductRepository.findByInvoice(last3Invoices.get(0));
            list.add(latestProduct_1.get(0));
        } else if (last3Invoices.size() == 2) {
            List<InvoiceProduct> latestProduct_1 = invoiceProductRepository.findByInvoice(last3Invoices.get(0));
            List<InvoiceProduct> latestProduct_2 = invoiceProductRepository.findByInvoice(last3Invoices.get(1));
            list.add(latestProduct_1.get(0));
            list.add(latestProduct_2.get(0));
        } else if (last3Invoices.size() >= 3) {
            List<InvoiceProduct> latestProduct_1 = invoiceProductRepository.findByInvoice(last3Invoices.get(0));
            List<InvoiceProduct> latestProduct_2 = invoiceProductRepository.findByInvoice(last3Invoices.get(1));
            List<InvoiceProduct> latestProduct_3 = invoiceProductRepository.findByInvoice(last3Invoices.get(2));
            list.add(latestProduct_1.get(0));
            list.add(latestProduct_2.get(0));
            list.add(latestProduct_3.get(0));
        }
        return list.stream().map(obj -> mapperUtil.convert(obj, new InvoiceProductDto())).collect(Collectors.toList());
    }

    @Override
    public void saveInvoiceProduct(InvoiceProductDto item) {
        Long id = item.getInvoice().getId();
        InvoiceProduct invoiceProduct = mapperUtil.convert(item, new InvoiceProduct());
        item.setTotal(getTotalPriceWithTax(invoiceProduct));
        invoiceProductRepository.save(invoiceProduct);
    }

    @Override
    public List<InvoiceProductDto> listAllByInvoiceIdAndCalculateTotalPrice(Long id) {
        return invoiceProductRepository.findAllByInvoiceId(id)
                .stream().map(invoice -> {
                    InvoiceProductDto dto = mapperUtil.convert(invoice, new InvoiceProductDto());
                    dto.setTotal(getTotalPriceWithTax(invoice));
                    return dto;
                }).toList();
    }

    @Override
    public void deleteInvoiceProduct(Long invoice, Long invoiceProduct) {
        InvoiceProduct item = invoiceProductRepository.findByIdAndInvoiceId(invoiceProduct, invoice);
        invoiceProductRepository.delete(item);
    }

    private BigDecimal getTotalPriceWithTax(InvoiceProduct invoiceProduct) {
        BigDecimal totalPrice = invoiceProduct.getPrice().multiply(BigDecimal.valueOf(invoiceProduct.getQuantity()));
        BigDecimal totalTax = totalPrice.multiply(BigDecimal.valueOf(invoiceProduct.getTax() / 100d));
        return totalPrice.add(totalTax);
    }

    @Override
    public Map<String, BigDecimal> getMonthlyProfitLoss() {
        UserDto userDto = securityService.getLoggedInUser();
        User user = userRepository.findByUsername(userDto.getUsername()).orElseThrow(()-> new UserNotFoundException("User not found"));
        List<Object[]> results = invoiceProductRepository.findMonthlyProfitLoss(user.getCompany().getId());
        return results.stream().collect(
                Collectors.toMap(
                        result -> (String) result[0],
                        result -> (BigDecimal) result[1],
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                )
        );
    }


}
