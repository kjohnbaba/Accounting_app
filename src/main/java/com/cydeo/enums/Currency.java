package com.cydeo.enums;

public enum Currency {

    EUR("eur"), USD("usd");

    private final String value;


    Currency(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    }

