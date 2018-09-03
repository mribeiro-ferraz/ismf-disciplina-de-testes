package com.mackleaps.formium.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ComponentNotFoundException extends IllegalStateException {

    public ComponentNotFoundException(String message){
        super(message);
    }

    public ComponentNotFoundException () {
        super();
    }

}
