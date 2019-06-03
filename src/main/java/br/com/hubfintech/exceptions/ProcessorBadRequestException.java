package br.com.hubfintech.exceptions;

@SuppressWarnings("serial")
public class ProcessorBadRequestException extends RuntimeException {

    public ProcessorBadRequestException(String message, Object... args) {
    	super(String.format(message, args));
    }
}
