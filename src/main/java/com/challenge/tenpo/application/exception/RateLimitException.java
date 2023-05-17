package com.challenge.tenpo.application.exception;

import com.challenge.tenpo.config.ErrorCode;
import com.challenge.tenpo.config.exception.GenericException;

public class RateLimitException extends GenericException {
    public RateLimitException(ErrorCode errorCode) {
        super(errorCode);
    }
}
