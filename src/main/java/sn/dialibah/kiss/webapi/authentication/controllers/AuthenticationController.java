package sn.dialibah.kiss.webapi.authentication.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sn.dialibah.kiss.webapi.authentication.models.LoginDataBean;
import sn.dialibah.kiss.webapi.authentication.models.SignupDataBean;
import sn.dialibah.kiss.webapi.authentication.services.AuthenticationService;
import sn.dialibah.kiss.webapi.authentication.services.IAuthenticationService;
import sn.dialibah.kiss.webapi.common.models.Profile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * by osow on 15/11/17.
 * for kiss-api
 */
@Slf4j
@RestController
@RequestMapping("auth")
public class AuthenticationController {

	private static final String LOG_HEADER = "[AUTHENTICATION]";

	private final IAuthenticationService authenticationService;

	@Autowired
	public AuthenticationController(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@RequestMapping(value = "signup", method = RequestMethod.POST)
	public ResponseEntity<Profile> signup(@RequestBody @Valid SignupDataBean signupDataBean) {
		log.info("{} Signing up user {}", LOG_HEADER, signupDataBean.getLogin());
		return new ResponseEntity<>(this.authenticationService.signup(signupDataBean), HttpStatus.OK);
	}

	@RequestMapping(value = "login", method = RequestMethod.POST)
	public ResponseEntity<Profile> login(@RequestBody @Valid LoginDataBean loginDataBean, HttpServletResponse response) {
		log.info("{} Logging in user {}", LOG_HEADER, loginDataBean.getLogin());

		Profile profile = this.authenticationService.login(loginDataBean);
		this.authenticationService.setJsonWebTokenUsingProfile(profile, response);
		return new ResponseEntity<>(profile, HttpStatus.OK);
	}

}
