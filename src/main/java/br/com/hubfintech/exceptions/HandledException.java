package br.com.hubfintech.exceptions;

import org.apache.commons.lang3.StringUtils;

import br.com.hubfintech.constants.ErrorCodes;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class HandledException extends RuntimeException {
	private final ErrorCodes code;
	private final String message;
	private final String details;
	private final String request;
	private final String response;

	public HandledException(ErrorCodes code, String details, String paramsMessage, Object... params) {
		this.code = code;
		this.message = code.toString().concat(" - ").concat(String.format(paramsMessage, params));
		this.details = details;
		this.request = StringUtils.EMPTY;
		this.response = StringUtils.EMPTY;
	}

	public HandledException(ErrorCodes code, String details, String request, String response, String paramsMessage, Object... params) {
		this.code = code;
		this.message = code.toString().concat(" - ").concat(String.format(paramsMessage, params));
		this.details = details;
		this.request = request;
		this.response = response;
	}

	public HandledException(Throwable cause, ErrorCodes code, String paramsMessage, Object... params) {
		this.code = code;
		this.message = code.toString().concat(" - ").concat(String.format(paramsMessage, params));
		this.request = StringUtils.EMPTY;
		this.response = StringUtils.EMPTY;
		this.initCause(cause);
		this.details = getDetailsFromCause(cause, StringUtils.EMPTY);
	}

	public HandledException(Throwable cause, ErrorCodes code, String details, String request, String response, String paramsMessage, Object... params) {
		this.code = code;
		this.message = code.toString().concat(" - ").concat(String.format(paramsMessage, params));
		this.request = request;
		this.response = response;
		this.initCause(cause);
		this.details = getDetailsFromCause(cause, details);
	}

	public String getFullErrorMessage() {
		return String.format("code: %s, message: %s, details: %s, request: %s, response: %s", code, message, details, request, response );
	}

	private String getDetailsFromCause(Throwable cause, String details) {
		String detailsWithCause = details;
		Throwable nextCause = cause;
		do {
			detailsWithCause = detailsWithCause.concat(String.format("%n, caused by: %s ", nextCause.toString()));
			nextCause = nextCause.getCause();
		} while(null != nextCause);

		return detailsWithCause;
	}
}
