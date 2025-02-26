package com.cydeo.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyDto {

    @JsonProperty("eur")
    private BigDecimal euro;
    @JsonProperty("gbp")
    private BigDecimal britishPound;
    @JsonProperty("inr")
    private BigDecimal indianRupee;
    @JsonProperty("jpy")
    private BigDecimal japaneseYen;
    @JsonProperty("cad")
    private BigDecimal canadianDollar;

}

