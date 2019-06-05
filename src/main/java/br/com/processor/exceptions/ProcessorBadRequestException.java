package br.com.processor.exceptions;

public class ProcessorBadRequestException extends RuntimeException {

    public ProcessorBadRequestException(String message, Object... args) {
        super(String.format(message, args));
    }

}
