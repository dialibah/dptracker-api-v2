package sn.dialibah.kiss.webapi.authentication.exceptions;

import org.springframework.http.HttpStatus;
import sn.dialibah.kiss.webapi.common.exceptions.AbstractException;

/**
 * by osow on 16/11/17.
 * for kiss-api
 */
public class InvalidPasswordException extends AbstractException {
	public InvalidPasswordException(String s) {
		super(s);
	}

	@Override
	public HttpStatus getStatus() {
		return HttpStatus.BAD_REQUEST;
	}

	@Override
	public String getErrorCode() {
		return "INVALID_PASSWORD";
	}
}
