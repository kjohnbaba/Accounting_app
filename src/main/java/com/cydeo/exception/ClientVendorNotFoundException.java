package com.cydeo.exception;

public class ClientVendorNotFoundException extends RuntimeException {
    public ClientVendorNotFoundException(String message) {
        super(message);
    }

    public ClientVendorNotFoundException() {
        super("This client or vendor does not exist");
    }
}
