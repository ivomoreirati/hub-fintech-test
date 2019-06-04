package br.com.hubfintech.exceptions;

public class ProcessorBadRequestException extends RuntimeException {

    public ProcessorBadRequestException() {
        super();
    }

    public ProcessorBadRequestException(String msg) {
        super(msg);
    }

    public ProcessorBadRequestException(String message, Object... args) {
    	super(String.format(message, args));
    }
}
