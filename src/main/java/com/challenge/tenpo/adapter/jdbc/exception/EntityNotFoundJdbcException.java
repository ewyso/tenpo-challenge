package com.challenge.tenpo.adapter.jdbc.exception;

import com.challenge.tenpo.config.ErrorCode;
import com.challenge.tenpo.config.exception.GenericException;

public class EntityNotFoundJdbcException extends GenericException {
    public EntityNotFoundJdbcException(ErrorCode errorCode) {
        super(errorCode);
    }
}
