package com.mackleaps.formium.exceptions;

public class DuplicatedComponentException extends IllegalStateException {

    public DuplicatedComponentException() { }

    public DuplicatedComponentException(String message) {
        super(message);
    }

}
