package com.mackleaps.formium.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SurveyComponentIsRootException extends IllegalStateException {

    public SurveyComponentIsRootException() { }

    public SurveyComponentIsRootException(String message) {
        super(message);
    }
}
