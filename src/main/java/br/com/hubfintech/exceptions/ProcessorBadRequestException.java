package br.com.hubfintech.exceptions;

public class ProcessorBadRequestException extends RuntimeException {

    public ProcessorBadRequestException(String msg) {
        super(msg);
    }

}
