package com.spinozanose.springbootrestoo.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class InvalidSearchParametersAdvice {

    @ResponseBody
    @ExceptionHandler(InvalidSearchParametersException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String objectNotFoundHandler(ObjectNotFoundException ex) {
        return ex.getMessage();
    }
}