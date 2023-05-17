package com.challenge.tenpo.adapter.rest.exception;

import com.challenge.tenpo.config.ErrorCode;
import com.challenge.tenpo.config.exception.GenericException;

public class PercentageRestException extends GenericException {
    public PercentageRestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
