package com.cydeo.exceptions;

public class InvoiceProductNotFoundException extends RuntimeException {

    public InvoiceProductNotFoundException(String message) {
        super(message);
    }
}
