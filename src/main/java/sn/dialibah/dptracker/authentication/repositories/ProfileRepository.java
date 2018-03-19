package sn.dialibah.dptracker.authentication.repositories;

import org.springframework.data.repository.CrudRepository;
import sn.dialibah.dptracker.authentication.entities.ProfileEntity;

/**
 * by osow on 15/11/17.
 * for kiss-api
 */
public interface ProfileRepository extends CrudRepository<ProfileEntity, String> {
	ProfileEntity findByLogin(String login);
}
