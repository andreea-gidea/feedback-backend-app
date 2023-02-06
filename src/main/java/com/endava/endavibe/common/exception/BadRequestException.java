package com.endava.endavibe.common.exception;

public class BadRequestException extends Exception {

    public BadRequestException(String errorMessage) {
        super(errorMessage);
    }
}
