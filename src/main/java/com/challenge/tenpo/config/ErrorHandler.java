package com.challenge.tenpo.config;

import com.challenge.tenpo.adapter.cache.exception.CacheSaveException;
import com.challenge.tenpo.adapter.jdbc.exception.EntityNotFoundJdbcException;
import com.challenge.tenpo.adapter.jdbc.exception.SqlReaderException;
import com.challenge.tenpo.adapter.rest.exception.BadRequestRestClientException;
import com.challenge.tenpo.adapter.rest.exception.RestClientGenericException;
import com.challenge.tenpo.adapter.rest.exception.TimeoutRestClientException;
import com.challenge.tenpo.application.exception.AdapterException;
import com.challenge.tenpo.application.exception.RateLimitException;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
@RestControllerAdvice
public class ErrorHandler {
    private final HttpServletRequest httpServletRequest;

    public ErrorHandler(final HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BadRequestRestClientException.class})
    public ResponseEntity<ApiErrorResponse> handle(BadRequestRestClientException ex) {
        log.error(HttpStatus.BAD_REQUEST.getReasonPhrase(), ex);
        return buildResponseError(HttpStatus.BAD_REQUEST, ex, ex.getCode());
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiErrorResponse> handle(Throwable ex) {
        log.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex);
        return buildResponseError(HttpStatus.INTERNAL_SERVER_ERROR, ex, ErrorCode.INTERNAL_ERROR);
    }

    @ExceptionHandler({CacheSaveException.class,})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiErrorResponse> handle(CacheSaveException ex) {
        log.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex);
        return buildResponseError(HttpStatus.INTERNAL_SERVER_ERROR, ex, ex.getCode());
    }

    @ExceptionHandler({RestClientGenericException.class})
    public ResponseEntity<ApiErrorResponse> handle(RestClientGenericException ex) {
        log.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex);
        return buildResponseError(HttpStatus.INTERNAL_SERVER_ERROR, ex, ex.getCode());
    }

    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    @ExceptionHandler(TimeoutRestClientException.class)
    public ResponseEntity<ApiErrorResponse> handle(TimeoutRestClientException ex) {
        log.error(HttpStatus.REQUEST_TIMEOUT.getReasonPhrase(), ex);
        return buildResponseError(HttpStatus.REQUEST_TIMEOUT, ex, ex.getCode());
    }

    @ExceptionHandler({AdapterException.class})
    public ResponseEntity<ApiErrorResponse> handle(AdapterException ex) {
        log.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex);
        return buildResponseError(HttpStatus.INTERNAL_SERVER_ERROR, ex, ex.getCode());
    }

    @ExceptionHandler(SqlReaderException.class)
    public ResponseEntity<ApiErrorResponse> handle(SqlReaderException ex) {
        log.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex);
        return buildResponseError(HttpStatus.INTERNAL_SERVER_ERROR, ex, ErrorCode.INTERNAL_ERROR);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ApiErrorResponse> handle(MethodArgumentNotValidException ex) {
        log.error(HttpStatus.BAD_REQUEST.getReasonPhrase(), ex);
        return buildResponseError(HttpStatus.BAD_REQUEST, ex, ErrorCode.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundJdbcException.class)
    public ResponseEntity<ApiErrorResponse> handle(EntityNotFoundJdbcException ex) {
        log.error(HttpStatus.NOT_FOUND.getReasonPhrase(), ex);
        return buildResponseError(HttpStatus.NOT_FOUND, ex, ex.getCode());
    }


    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    @ExceptionHandler(RateLimitException.class)
    public ResponseEntity<ApiErrorResponse> handle(RateLimitException ex) {
        log.error(HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase(), ex);
        return buildResponseError(HttpStatus.TOO_MANY_REQUESTS, ex, ex.getCode());
    }

    private ResponseEntity<ApiErrorResponse> buildResponseError(HttpStatus httpStatus, Throwable ex, ErrorCode errorCode) {

        final var apiErrorResponse = ApiErrorResponse
            .builder()
            .timestamp(LocalDateTime.now())
            .name(httpStatus.getReasonPhrase())
            .detail(String.format("%s: %s", ex.getClass().getCanonicalName(), ex.getMessage()))
            .status(httpStatus.value())
            .code(errorCode.value())
            .resource(httpServletRequest.getRequestURI())
            .build();

        return new ResponseEntity<>(apiErrorResponse, httpStatus);
    }

    @Builder
    @NonNull
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    private static class ApiErrorResponse {

        private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss[.SSSSSSSSS]['Z']";

        @JsonProperty
        private String name;
        @JsonProperty
        private Integer status;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
        private LocalDateTime timestamp;
        @JsonProperty
        private Integer code;
        @JsonProperty
        private String resource;
        @JsonProperty
        private String detail;
    }
}

