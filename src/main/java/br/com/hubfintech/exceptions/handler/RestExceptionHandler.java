package br.com.hubfintech.exceptions.handler;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.MappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.hubfintech.constants.ErrorCodes;
import br.com.hubfintech.exceptions.HandledException;
import br.com.hubfintech.exceptions.ExceptionResponse;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger superClassLogger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        superClassLogger.error("An unexpected error occurred: {} ", ex.getMessage(), ex);

        ExceptionResponse exceptionResponse = new ExceptionResponse(ErrorCodes.INTERNAL_SERVER_ERROR, ex);

        request.getDescription(false);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        superClassLogger.error("Invalid Arguments: {} ", ex.getMessage(), ex);

        ExceptionResponse exceptionResponse = new ExceptionResponse(ErrorCodes.INVALID_REQUEST, ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        superClassLogger.error("MethodArgumentNotValidException {} ", ex.getMessage(), ex);

        final List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<String> errors = new ArrayList<>();
        fieldErrors.forEach(f ->
                errors.add(String.format("%s : %s", f.getField(), f.getDefaultMessage()))
        );

        ExceptionResponse exceptionResponse = new ExceptionResponse(ErrorCodes.BAD_REQUEST, errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

    @ExceptionHandler(HandledException.class)
    public ResponseEntity<Object> handleHandledException(HandledException ex) {
        logger.error(ex.getFullErrorMessage(), ex);

        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getCode(), ex.getMessage(), ex.getDetails());

        return ResponseEntity.status(exceptionResponse.getHttpStatus()).body(exceptionResponse);
    }

    @ExceptionHandler(MappingException.class)
    public ResponseEntity<Object> handleModelMapperConverterException(MappingException ex) {
        logger.error(ErrorCodes.MAPPING_EXCEPTION, ex);

        ExceptionResponse exceptionResponse = new ExceptionResponse(ErrorCodes.MAPPING_EXCEPTION, ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse);
    }
}