package com.cydeo.dto.common;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

// no need to create this dto, we can directly map the consumed values to CurrencyDto
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsdDto {


    private BigDecimal cad;
    private BigDecimal eur;
    private BigDecimal gbp;
    private BigDecimal inr;
    private BigDecimal jpy;


}