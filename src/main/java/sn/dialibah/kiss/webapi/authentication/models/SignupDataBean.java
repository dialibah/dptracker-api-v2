package sn.dialibah.kiss.webapi.authentication.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * by osow on 15/11/17.
 * for kiss-api
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupDataBean {

	@NotNull
	private String login;

	@NotNull
	private String password;

	@NotNull
	private String confirmPassword;

	private String firstName, lastName;
}
