package com.challenge.tenpo.adapter.cache.exception;

import com.challenge.tenpo.config.ErrorCode;
import com.challenge.tenpo.config.exception.GenericException;

public class CacheSaveException extends GenericException {
    public CacheSaveException(ErrorCode errorCode) {
        super(errorCode);
    }
}
