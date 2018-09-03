package com.mackleaps.formium.exceptions;

public class InvalidTokenException extends Exception {

    public InvalidTokenException() { }

    public InvalidTokenException(String message){
        super(message);
    }
}
