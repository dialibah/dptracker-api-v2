package sn.dialibah.dptracker.common.models;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

/**
 * Author: klabesse
 */
@Data
@Builder
public class SiCallContext {
	private String url;
	private HttpMethod method;
	private String payload;
	private HttpStatus responseStatus;
	private MediaType responseContentType;
	private String responseBody;

	public String printCallContext() {
		String res = String.format("request url : %s\n" + "request method : %s\n", url, method);
		if(StringUtils.isNotBlank(payload)){
			res += String.format("request body :\n\t%s\n", payload);
		}
		res += "----\n";
		res += String.format("response status : %s\n", responseStatus);
		res += String.format("response content-type : %s\n", responseContentType);
		if(StringUtils.isNotBlank(responseBody)){
			res += String.format("response body :\n\t%s\n", responseBody);
		}
		res += "\n\n";
		return res;
	}
}
