package com.cydeo.enums;

public enum PaymentStatus {

        PAID("Paid"),
        PAY("Pay");

        private final String value;

        PaymentStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
}
