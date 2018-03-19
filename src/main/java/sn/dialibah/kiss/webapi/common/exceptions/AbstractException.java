package sn.dialibah.kiss.webapi.common.exceptions;

import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Author: osow
 */
@ToString
public abstract class AbstractException extends RuntimeException {

	protected ResponseEntity forwardedResponse;

	private String className;

	/**
	 * ${@inheritDoc}
	 */
	public AbstractException(String message) {
		super(message);
	}

	/**
	 * Constructor to build {@link AbstractException} with a message and the class name throwing exception
	 *
	 * @param message   indication {@link String} to be displayed to end user
	 * @param className {@link String} to be logged
	 */
	public AbstractException(String message, String className) {
		super(message);

		this.className = className;
	}

	/**
	 * ${@inheritDoc}
	 */
	public AbstractException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor to build {@link AbstractException} with a message and existing {@link ResponseEntity}
	 *
	 * @param message        indication {@link String} to be displayed to end user
	 * @param responseEntity {@link ResponseEntity} to be returned by api
	 */
	public AbstractException(String message, ResponseEntity responseEntity) {
		super(message);
		this.forwardedResponse = responseEntity;
	}

	public abstract HttpStatus getStatus();

	public abstract String getErrorCode();

	public ResponseEntity getForwardedResponse() {
		return this.forwardedResponse;
	}

	/**
	 * Check if exception contains inner forwarded response to process
	 *
	 * @return true if {@code forwardedResponse} is not empty, false else
	 */
	public boolean hasForwardedResponse() {
		return forwardedResponse != null;
	}

	public String getDeveloperMessage() {
		StringBuilder sb = new StringBuilder(getMessage());
		if (getCause() != null &&
						StringUtils.isNotBlank(getCause().getMessage()))
			sb.append(':').append(getCause().getMessage());
		return sb.toString();
	}

	public String getClassName() {
		return className;
	}
}