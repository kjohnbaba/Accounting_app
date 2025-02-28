package com.cydeo.enums;

public enum InvoiceType {

    PURCHASE("Purchase"), SALES("Sales");

    private final String value;

    private InvoiceType(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }

}
