package sn.dialibah.dptracker.authentication.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * by osow on 16/11/17.
 * for kiss-api
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenEntity {

	@NotNull
	private String login;

	@NotNull
	private String jsonWebToken;

}
