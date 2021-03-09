package com.orange.neptunev2.backend_registry.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class BackendNotFoundException extends RuntimeException{

    public BackendNotFoundException(String exception){
        super(exception);
    }

    public BackendNotFoundException(){

    }
}
