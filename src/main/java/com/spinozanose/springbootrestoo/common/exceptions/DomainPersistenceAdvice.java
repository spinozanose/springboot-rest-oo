package com.spinozanose.springbootrestoo.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class DomainPersistenceAdvice {

    @ResponseBody
    @ExceptionHandler(DomainPersistenceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String failedDomainPersistenceHandler(DomainPersistenceException ex) {
        return ex.getMessage();
    }
}
