package sn.dialibah.kiss.webapi.common.configurations.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Created by OSOW
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private final AuthenticationFilter authenticationFilter;

	@Autowired
	public WebSecurityConfig(AuthenticationFilter authenticationFilter) {
		this.authenticationFilter = authenticationFilter;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
						.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
						.and()
						.authorizeRequests()
						.antMatchers(HttpMethod.POST, "/auth/login").permitAll()
						.antMatchers(HttpMethod.POST, "/auth/signup").permitAll()
						.antMatchers("/common/ping").permitAll()
						.anyRequest().authenticated()
						.and()
						.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
		;
	}
}
