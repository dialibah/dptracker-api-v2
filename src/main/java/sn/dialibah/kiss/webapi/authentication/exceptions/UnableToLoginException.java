package sn.dialibah.kiss.webapi.authentication.exceptions;

import org.springframework.http.HttpStatus;
import sn.dialibah.kiss.webapi.common.exceptions.AbstractException;

/**
 * by osow on 16/11/17.
 * for kiss-api
 */
public class UnableToLoginException extends AbstractException {
	public UnableToLoginException(String message) {
		super(message);
	}

	@Override
	public HttpStatus getStatus() {
		return HttpStatus.UNAUTHORIZED;
	}

	@Override
	public String getErrorCode() {
		return "LOGIN_FAILED";
	}
}
