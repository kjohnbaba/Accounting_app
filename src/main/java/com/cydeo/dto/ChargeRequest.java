package com.cydeo.dto;

import com.cydeo.enums.Currency;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
    public class ChargeRequest {
        private String description;
        private int amount;
        private Currency currency;
        private String stripeEmail;
        private String stripeToken;
    }


