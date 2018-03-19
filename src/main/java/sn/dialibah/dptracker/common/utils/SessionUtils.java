package sn.dialibah.dptracker.common.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import sn.dialibah.dptracker.common.models.RequestContext;

import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * Author: klabesse
 */
public class SessionUtils {

	private static final String CONTEXT = "GPS_CONTEXT";

	public static HttpSession getOrCreateSession() {
		ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
		return attr.getRequest().getSession(true);
	}

	public static HttpSession getCurrentSession() {
		ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
		return attr.getRequest().getSession(false);
	}

	public static Optional<RequestContext> getContext() {
		try{
			ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
			HttpSession session = attr.getRequest().getSession(false);
			if(session == null)
				return Optional.empty();
			RequestContext context = (RequestContext)session.getAttribute(CONTEXT);
			return context != null ? Optional.of(context) : Optional.empty();
		}catch(IllegalStateException ise){
			// not in request context
			return Optional.empty();
		}
	}

	public static void setContext(RequestContext context) {
		if(context != null){
			ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
			HttpSession session = attr.getRequest().getSession(true);
			session.setAttribute(CONTEXT, context);
		}
	}
}