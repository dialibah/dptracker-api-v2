package sn.dialibah.kiss.webapi.common.configurations.security.services;

import sn.dialibah.kiss.webapi.common.configurations.security.models.TokenWrapper;
import sn.dialibah.kiss.webapi.common.models.Profile;

/**
 * by osow on 15/11/17.
 * for kiss-api
 */
public interface ICryptoService {
	/**
	 * Crypt a given password
	 *
	 * @param password password to crypt
	 * @return encrypted password
	 */
	String encryptPassword(String password);

	/**
	 * Check if a given password is equals to
	 * an encrypted one
	 *
	 * @param passwordToTest the pwd to check
	 * @param realPassword   the actual password
	 * @return a boolean
	 */
	boolean checkPassword(String passwordToTest, String realPassword);

	/**
	 * Create a Json Web Token from profile
	 *
	 * @param profile the profile
	 * @return the JWT
	 */
	String createTokenFromProfile(Profile profile);

	/**
	 * Save an issued token
	 *
	 * @param login user profile login
	 * @param jwt   issued token
	 * @return count of token issued to the given profile
	 */
	Long persistTokenInDB(String login, String jwt);

	/**
	 * Get encoded information from a JsonWebToken
	 *
	 * @param token the token
	 * @return a {@link TokenWrapper}
	 */
	TokenWrapper parseUserProfileFromToken(String token);
}
