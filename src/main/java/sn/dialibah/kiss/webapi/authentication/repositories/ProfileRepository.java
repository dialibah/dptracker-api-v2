package sn.dialibah.kiss.webapi.authentication.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import sn.dialibah.kiss.webapi.authentication.entities.ProfileEntity;

/**
 * by osow on 15/11/17.
 * for kiss-api
 */
public interface ProfileRepository extends PagingAndSortingRepository<ProfileEntity, String> {
	ProfileEntity findByLogin(String login);
}
