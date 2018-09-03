package com.mackleaps.formium.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ParentNotFoundException extends IllegalStateException {

    public ParentNotFoundException(String message){
        super(message);
    }
    public ParentNotFoundException(){ super(); }

}



