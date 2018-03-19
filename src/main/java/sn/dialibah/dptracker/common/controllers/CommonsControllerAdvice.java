package sn.dialibah.dptracker.common.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sn.dialibah.dptracker.common.exceptions.AbstractException;
import sn.dialibah.dptracker.common.utils.SessionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Author: klabesse
 */
@Slf4j
@ControllerAdvice
@SuppressWarnings("unused")
public class CommonsControllerAdvice {

	@ExceptionHandler(Exception.class)
	public ResponseEntity otherExceptionsHandler(Exception e, HttpServletRequest request) {
		Map<String, Object> error = new HashMap<>();
		error.put("timestamp", (new Date()).getTime());
		error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
		if(e.getMessage() != null)
			error.put("message", e.getMessage());
		error.put("exception", e.getClass().getName());
		if(e.getCause() != null){
			StringWriter stackTrace = new StringWriter();
			e.getCause().printStackTrace(new PrintWriter(stackTrace));
			stackTrace.flush();
			error.put("trace", stackTrace.toString());
		}
		String path = request.getRequestURI();
		if(path != null){
			error.put("path", path);
		}

		log.error("[EXCEPTIONS] Exception handler", e);
		SessionUtils.getContext().ifPresent(requestContext -> requestContext.printContext("error"));

		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(AbstractException.class)
	public ResponseEntity exceptionsHandler(AbstractException e, HttpServletRequest request) {
		if(e.hasForwardedResponse())
			return e.getForwardedResponse();

		Map<String, Object> error = new HashMap<>();
		error.put("timestamp", (new Date()).getTime());
		error.put("status", e.getStatus().value());
		if(e.getMessage() != null)
			error.put("message", e.getMessage());
		error.put("error", e.getErrorCode());
		error.put("exception", e.getClass().getName());
		if(e.getCause() != null){
			StringWriter stackTrace = new StringWriter();
			e.getCause().printStackTrace(new PrintWriter(stackTrace));
			stackTrace.flush();
			error.put("trace", stackTrace.toString());
		}
		String path = request.getRequestURI();
		if(path != null){
			error.put("path", path);
		}

		if(e.getStatus() == HttpStatus.IM_USED || e.getStatus() == HttpStatus.BAD_REQUEST || e.getStatus().is5xxServerError()){
			log.error("[EXCEPTIONS][{}] Handle custom exception -> path : {}", e.getClassName(), request.getRequestURI(), e);
			SessionUtils.getContext().ifPresent(requestContext -> requestContext.printContext("error"));
		}
		else{
			log.info("[EXCEPTIONS][{}] Handle custom exception -> path : {}", e.getClassName(), request.getRequestURI(), e);
		}

		return new ResponseEntity<>(error, e.getStatus());
	}

	public static ResponseEntity displayValidationErrors(BindingResult bindingResult) {
		List<String> errors = bindingResult.getAllErrors()
						.stream()
						.map(e -> String.format("Field %s of object %s is not valid, cause : %s", ((FieldError)e).getField(), e.getObjectName(), e.getDefaultMessage()))
						.collect(Collectors.toList());
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}
}