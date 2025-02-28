package com.cydeo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/").setViewName("login");
        registry.addViewController("/users/list").setViewName("user/user-list");
        registry.addViewController("/dashboard").setViewName("dashboard");
        registry.addViewController("/companies/list").setViewName("company/company-list");
        registry.addViewController("/clientVendors/list").setViewName("clientVendor/clientVendor-list");
        registry.addViewController("/categories/list").setViewName("category/category-list");
        registry.addViewController("/products/list").setViewName("product/product-list");
        registry.addViewController("/purchaseInvoices/list").setViewName("invoice/purchase-invoice-list");
        registry.addViewController("/salesInvoices/list").setViewName("invoice/sales-invoice-list");
    }


}
