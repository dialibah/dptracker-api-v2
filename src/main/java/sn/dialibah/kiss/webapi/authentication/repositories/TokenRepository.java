package sn.dialibah.kiss.webapi.authentication.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import sn.dialibah.kiss.webapi.authentication.entities.TokenEntity;

/**
 * by osow on 16/11/17.
 * for kiss-api
 */
public interface TokenRepository extends PagingAndSortingRepository<TokenEntity, String> {
	Long countByLogin(String login);

	TokenEntity findByJsonWebToken(String token);
}
