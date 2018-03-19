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
public class LoginDataBean {

	@NotNull
	private String login;

	@NotNull
	private String password;

}
