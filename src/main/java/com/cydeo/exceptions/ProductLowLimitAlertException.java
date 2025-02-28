package com.cydeo.exceptions;

public class ProductLowLimitAlertException extends RuntimeException {

    public ProductLowLimitAlertException(String message) {
        super(message);
    }
}
