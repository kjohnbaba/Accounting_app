package com.cydeo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle ProductLowLimitAlertException
    @ExceptionHandler(ProductLowLimitAlertException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleProductLowLimitAlertException(ProductLowLimitAlertException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "error";
    }

    // Handle ProductNotFoundException
    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)  // Set HTTP status 400 Bad Request
    public String handleProductNotFoundException(ProductNotFoundException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "error";
    }

    // Handle UserNotFoundException
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleUserNotFoundException(UserNotFoundException ex, Model model){
        model.addAttribute("message", ex.getMessage());
        return "error";
    }

   // Handle InvoiceProductNotFoundException
    @ExceptionHandler(InvoiceProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleInvoiceProductNotFoundException(InvoiceProductNotFoundException ex, Model model){
        model.addAttribute("message", ex.getMessage());
        return "error";
    }


}
