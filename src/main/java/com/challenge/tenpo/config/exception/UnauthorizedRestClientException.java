package com.challenge.tenpo.config.exception;

import com.challenge.tenpo.config.ErrorCode;

public class UnauthorizedRestClientException extends GenericException {
    public UnauthorizedRestClientException(ErrorCode errorCode) {
        super(errorCode);
    }
}
