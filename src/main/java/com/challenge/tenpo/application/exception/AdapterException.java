package com.challenge.tenpo.application.exception;


import com.challenge.tenpo.config.ErrorCode;
import com.challenge.tenpo.config.exception.GenericException;

public class AdapterException extends GenericException {

    public AdapterException(ErrorCode ec){ super(ec);}
}
