package sn.dialibah.kiss.webapi.common.configurations.security.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sn.dialibah.kiss.webapi.authentication.entities.TokenEntity;
import sn.dialibah.kiss.webapi.authentication.exceptions.TokenNotValidException;
import sn.dialibah.kiss.webapi.authentication.repositories.TokenRepository;
import sn.dialibah.kiss.webapi.common.configurations.security.models.TokenWrapper;
import sn.dialibah.kiss.webapi.common.models.Profile;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

/**
 * by osow on 15/11/17.
 * for kiss-api
 */
@Slf4j
@Service
public class CryptoService implements ICryptoService {

	private static final String LOG_HEADER = "[CRYPTO SERVICE]";

	private final TokenRepository tokenRepository;

	private Algorithm algoHS;

	@Value("${kiss.security.salt}")
	private String salt;

	@Value("${kiss.security.jwt-duration}")
	private String duration;

	@Value("${kiss.security.jwt-claim-key}")
	private String jwtClaimKey;

	@Value("${kiss.security.jwt-claim-ttl-key}")
	private String jwtClaimTTLKey;

	@Value("${kiss.security.jwt-issuer}")
	private String jwtIssuer;
	private JWTVerifier verifier;

	@Autowired
	public CryptoService(@Value("${kiss.security.jwt-secret}") String secret, TokenRepository tokenRepository) {
		try {
			this.algoHS = Algorithm.HMAC512(secret);
			this.verifier = JWT.require(algoHS).withIssuer(jwtIssuer).build();
		} catch (UnsupportedEncodingException e) {
			log.error("{} failed to init HMAC512 algorithm {}", LOG_HEADER, e);
		}
		this.tokenRepository = tokenRepository;
	}


	@Override
	public String encryptPassword(String password) {
		return DigestUtils.md5Hex(password + salt);
	}

	@Override
	public boolean checkPassword(String passwordToTest, String realPassword) {
		return !StringUtils.isBlank(realPassword) && realPassword.equals(DigestUtils.md5Hex(passwordToTest + salt));
	}

	@Override
	public String createTokenFromProfile(Profile profile) {
		String expirationDateTime = LocalDateTime.now().plusSeconds(Long.parseLong(this.duration)).toString();
		if (StringUtils.isBlank(profile.getLogin())) {
			return null;
		}
		String jwt = JWT.create()
						.withClaim(jwtClaimKey, profile.getLogin())
						.withClaim(jwtClaimTTLKey, expirationDateTime)
						.withIssuer(jwtIssuer)
						.sign(algoHS);
		log.trace("{} Generated token for user {} is : {}", LOG_HEADER, profile.getLogin(), jwt);
		return jwt;
	}

	@Override
	public Long persistTokenInDB(String login, String jwt) {
		this.tokenRepository.save(TokenEntity.builder()
						.login(login)
						.jsonWebToken(jwt)
						.build());
		return this.tokenRepository.countByLogin(login);
	}

	@Override
	public TokenWrapper parseUserProfileFromToken(String token) {

		try {
			DecodedJWT decodedJWT = verifyAndDecodeToken(token);
			String profileLogin = decodedJWT.getClaims().get(jwtClaimKey).asString();
			String expirationDateTime = decodedJWT.getClaims().get(jwtClaimTTLKey).asString();
			log.trace("{} jwt : {} for {} expire at {}", LOG_HEADER, profileLogin, expirationDateTime);
			return TokenWrapper.builder()
							.expirationDate(expirationDateTime)
							.profileId(profileLogin)
							.build();
		} catch (TokenExpiredException tee) {
			log.debug("{} token {} expired", LOG_HEADER, token);
			throw new TokenNotValidException("Expired Token");
		} catch (JWTVerificationException jve) {
			log.debug("{} failed to verify token {}", LOG_HEADER, token);
			throw new TokenNotValidException("Failed to verify token");
		}
	}

	private DecodedJWT verifyAndDecodeToken(String token) {
		TokenEntity issuedToken = this.tokenRepository.findByJsonWebToken(token);
		if (issuedToken == null) throw new TokenNotValidException("Token revoked or never issued");
		return verifier.verify(token);
	}

}
