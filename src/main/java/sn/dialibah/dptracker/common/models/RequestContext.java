package sn.dialibah.dptracker.common.models;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: klabesse
 */
@Slf4j
@Data
@Builder
public class RequestContext {
	private String url;
	private HttpMethod method;
	private String body;
	private List<SiCallContext> callContexts = new ArrayList<>();

	public void printContext(String level) {
		switch(level){
			case "trace":
				log.trace(prepareContext());
				break;
			case "debug":
				log.debug(prepareContext());
				break;
			case "info":
				log.info(prepareContext());
				break;
			case "warn":
				log.warn(prepareContext());
				break;
			case "error":
				log.error(prepareContext());
				break;
		}
	}

	private String prepareContext() {
		String res = String.format("\n=========== CONTEXT BEGIN ===========\n" + "request url : %s\n" + "request method : %s\n", url, method);
		if(StringUtils.isNotBlank(body)){
			res += String.format("request body :\n\t%s\n", body);
		}

		if(CollectionUtils.isNotEmpty(callContexts)){
			res += "\n----------- SI CALLS -----------\n\n";

			res += callContexts.stream().map(SiCallContext::printCallContext).collect(Collectors.joining("\n-----------\n"));
		}

		res += "=========== CONTEXT END ===========\n\n";
		return res;
	}
}
