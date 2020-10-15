package com.spinozanose.springbootrestoo.implementation.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class InvalidDomainDataAdvice {

    @ResponseBody
    @ExceptionHandler(InvalidDomainDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String invalidDomainDataHandler(InvalidDomainDataException ex) {
        return ex.getMessage();
    }
}

