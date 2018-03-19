package sn.dialibah.kiss.webapi.common.configurations.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import sn.dialibah.kiss.webapi.authentication.services.IAuthenticationService;
import sn.dialibah.kiss.webapi.common.configurations.security.models.TokenWrapper;
import sn.dialibah.kiss.webapi.common.configurations.security.services.ICryptoService;
import sn.dialibah.kiss.webapi.common.exceptions.AbstractException;
import sn.dialibah.kiss.webapi.common.models.Profile;
import sn.dialibah.kiss.webapi.common.models.UserAuthentication;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * by osow on 15/11/17.
 * for kiss-api
 */

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthenticationFilter extends GenericFilterBean {

	private final static String LOG_HEADER = "[SECURITY][AUTHENTICATION]";

	private final ICryptoService cryptoService;

	private final IAuthenticationService authenticationService;

	@Value("${kiss.token.header.name}")
	private String TOKEN_HEADER_NAME;

	@Autowired
	public AuthenticationFilter(ICryptoService cryptoService, IAuthenticationService authenticationService) {
		this.cryptoService = cryptoService;
		this.authenticationService = authenticationService;
	}


	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws
					IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		final String token = request.getHeader(TOKEN_HEADER_NAME);
		if (StringUtils.isBlank(token)) {
			log.trace("{} No token found in request header", LOG_HEADER);
			filterChain.doFilter(request, response);
			return;
		}

		TokenWrapper tokenWrapper;
		try {
			tokenWrapper = cryptoService.parseUserProfileFromToken(token);
			if (LocalDateTime.now().isAfter(LocalDateTime.parse(tokenWrapper.getExpirationDate()))) {
				log.trace("{} The token {} of {} has expired", LOG_HEADER, token, tokenWrapper.getProfileId());
				filterChain.doFilter(request, response);
				return;
			}

		} catch (AbstractException ae) {
			log.trace("{} Failed to parse token {}", LOG_HEADER, token);
			filterChain.doFilter(request, response);
			return;
		}

		Authentication authentication = getAuthentication(tokenWrapper.getProfileId());
		if (authentication != null)
			SecurityContextHolder.getContext().setAuthentication(authentication);
		response.setHeader(TOKEN_HEADER_NAME, token);

		filterChain.doFilter(request, response);

	}

	/**
	 * Get Authentication from user profile
	 *
	 * @param profileId user profile id
	 * @return a {@link UserAuthentication}
	 */
	private Authentication getAuthentication(String profileId) {
		Optional<Profile> profileOpt = this.authenticationService.getProfile(profileId);
		return profileOpt.map(this::authenticationFromProfile).orElse(null);
	}

	private Authentication authenticationFromProfile(Profile profile) {
		return new UserAuthentication(profile.getLogin(), profile);
	}
}
