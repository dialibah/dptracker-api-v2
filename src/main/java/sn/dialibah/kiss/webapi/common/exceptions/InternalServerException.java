package sn.dialibah.kiss.webapi.common.exceptions;

import org.springframework.http.HttpStatus;

/**
 * by osow on 16/11/17.
 * for kiss-api
 */
public class InternalServerException extends AbstractException {

	public InternalServerException(String s) {
		super(s);
	}

	@Override
	public HttpStatus getStatus() {
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}

	@Override
	public String getErrorCode() {
		return "INTERNAL_SERVER_ERROR";
	}
}
