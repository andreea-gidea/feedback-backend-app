package com.endava.endavibe.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private String message;

    @ExceptionHandler(value
            = Exception.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse
    handleCustomerAlreadyExistsException(
            Exception ex) {
        return new ErrorResponse(
                ex.getMessage());
    }
}
