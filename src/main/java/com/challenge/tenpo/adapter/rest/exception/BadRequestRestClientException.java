package com.challenge.tenpo.adapter.rest.exception;


import com.challenge.tenpo.config.ErrorCode;
import com.challenge.tenpo.config.exception.GenericException;

public final class BadRequestRestClientException extends GenericException {

    public BadRequestRestClientException(ErrorCode errorCode) {
        super(errorCode);
    }
}
