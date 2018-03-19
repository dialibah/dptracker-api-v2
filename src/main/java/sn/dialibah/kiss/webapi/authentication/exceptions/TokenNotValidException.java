package sn.dialibah.kiss.webapi.authentication.exceptions;

import org.springframework.http.HttpStatus;
import sn.dialibah.kiss.webapi.common.exceptions.AbstractException;

/**
 * by osow on 16/11/17.
 * for kiss-api
 */
public class TokenNotValidException extends AbstractException {

	public TokenNotValidException(String message) {
		super(message);
	}

	@Override
	public HttpStatus getStatus() {
		return HttpStatus.FORBIDDEN;
	}

	@Override
	public String getErrorCode() {
		return "TOKEN_INVALID";
	}
}
