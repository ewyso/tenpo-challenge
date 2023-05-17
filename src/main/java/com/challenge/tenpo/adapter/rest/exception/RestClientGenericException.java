package com.challenge.tenpo.adapter.rest.exception;

import com.challenge.tenpo.config.ErrorCode;
import com.challenge.tenpo.config.exception.GenericException;

public final class RestClientGenericException extends GenericException {

    public RestClientGenericException(ErrorCode errorCode) {
        super(errorCode);
    }
}
