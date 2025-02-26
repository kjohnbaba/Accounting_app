package com.cydeo.exception;

public class InvoiceProductNotFoundException extends RuntimeException {

    public InvoiceProductNotFoundException(String message) {
        super(message);
    }

    public InvoiceProductNotFoundException() {
        super("InvoiceProduct not found");
    }
}
