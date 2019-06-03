package br.com.hubfintech.exceptions;

import br.com.hubfintech.constants.ErrorCodes;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
public class ExceptionResponse implements Serializable {

    private String code;
    private String message;
    private List<String> details;

    @JsonIgnore
    private HttpStatus httpStatus;

    public ExceptionResponse(final ErrorCodes errorCode, Throwable cause) {
        this.code = errorCode.name();
        this.message = cause.getMessage();
        this.details = getListFromCause(cause);
        this.httpStatus = getStatusFromErrorCode(errorCode);
    }

    public ExceptionResponse(final ErrorCodes errorCode, String message, String details) {
        this.code = errorCode.name();
        this.message = message;
        this.details = getListFromString(details);
        this.httpStatus = getStatusFromErrorCode(errorCode);
    }

    public ExceptionResponse(final ErrorCodes errorCode, String details) {
        this.code = errorCode.name();
        this.message = errorCode.name();
        this.details = getListFromString(details);
        this.httpStatus = getStatusFromErrorCode(errorCode);
    }

    public ExceptionResponse(ErrorCodes errorCode, List<String> details) {
        this.code = errorCode.name();
        this.message = errorCode.name();
        this.details = details;
        this.httpStatus = getStatusFromErrorCode(errorCode);
    }

    private List<String> getListFromString(String input) {
        if (!input.isEmpty() && (input.contains("\r") || input.contains("\n"))) {
            String[] lines = input.split("\\r?\\n");
            return Arrays.asList(lines);
        }
        return Collections.singletonList(input);
    }

    private List<String> getListFromCause(Throwable cause) {
        List<String> messages = new ArrayList<>();

        Throwable nextCause = cause;
        do {
            messages.add(String.format("caused by: %s ", nextCause.toString().replaceAll("\\n", " ").replaceAll("\\r", "")));
            nextCause = nextCause.getCause();
        } while (null != nextCause);

        return messages;
    }

    private HttpStatus getStatusFromErrorCode(final ErrorCodes errorCode) {
        switch (errorCode) {
            case INVALID_REQUEST:
            case BAD_REQUEST:
            case INTERNAL_SERVER_ERROR:
            default:
                return HttpStatus.INTERNAL_SERVER_ERROR;
        }

    }
}