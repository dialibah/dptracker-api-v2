package sn.dialibah.dptracker.authentication.repositories;

import sn.dialibah.dptracker.authentication.entities.TokenEntity;

/**
 * by osow on 16/11/17.
 * for kiss-api
 */
public interface TokenRepository {
	Long countByLogin(String login);

	TokenEntity findByJsonWebToken(String token);
}
