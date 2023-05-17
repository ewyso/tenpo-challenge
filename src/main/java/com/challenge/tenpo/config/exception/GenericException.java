package com.challenge.tenpo.config.exception;


import com.challenge.tenpo.config.ErrorCode;

public abstract class GenericException extends RuntimeException {

    private static final String SPACE = " ";
    private static final String COMMA = ",";
    private final ErrorCode errorCode;

    public GenericException(ErrorCode errorCode) {
        super(errorCode.getReasonPhrase());
        this.errorCode = errorCode;
    }

    public GenericException(ErrorCode errorCode, String message) {
        super(buildMessage(message, errorCode));
        this.errorCode = errorCode;
    }

    public GenericException(ErrorCode errorCode, String message, Throwable cause) {
        super(buildMessage(message, errorCode), cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getCode() {
        return this.errorCode;
    }

    private static String buildMessage(String message, ErrorCode errorCode) {
        return errorCode.getReasonPhrase() + COMMA + SPACE + message;
    }

}
