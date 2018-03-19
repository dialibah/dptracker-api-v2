package sn.dialibah.dptracker.common.configurations.security.models;

import lombok.Builder;
import lombok.Data;

/**
 * by osow on 15/11/17.
 * for kiss-api
 */
@Data
@Builder
public class TokenWrapper {

	private String profileId;

	private String expirationDate;

}
