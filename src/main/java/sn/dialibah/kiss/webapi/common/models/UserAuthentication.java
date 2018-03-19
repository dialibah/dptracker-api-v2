package sn.dialibah.kiss.webapi.common.models;

import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * by osow on 16/11/17.
 * for kiss-api
 */
@Data
public class UserAuthentication implements Authentication {

	private final String principal;
	private final Profile profile;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getDetails() {
		return profile;
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}

	@Override
	public boolean isAuthenticated() {
		return true;
	}

	@Override
	public void setAuthenticated(boolean b) throws IllegalArgumentException {

	}

	@Override
	public String getName() {
		return principal;
	}
}
