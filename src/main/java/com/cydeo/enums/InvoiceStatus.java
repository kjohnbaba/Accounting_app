package com.cydeo.enums;

public enum InvoiceStatus {

    AWAITING_APPROVAL("Awaiting Approval"), APPROVED("Approved");

    //value
    private final String value;

    //constructor
    InvoiceStatus(String value) {
        this.value = value;
    }
    //get() method to implement later in our code
    public String getValue() {
        return value;
    }

}
