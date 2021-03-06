package sn.dialibah.dptracker.authentication.services;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import sn.dialibah.dptracker.authentication.entities.ProfileEntity;
import sn.dialibah.dptracker.authentication.exceptions.InvalidPasswordException;
import sn.dialibah.dptracker.authentication.exceptions.UnableToLoginException;
import sn.dialibah.dptracker.authentication.models.LoginDataBean;
import sn.dialibah.dptracker.authentication.models.SignupDataBean;
import sn.dialibah.dptracker.authentication.repositories.ProfileRepository;
import sn.dialibah.dptracker.common.configurations.security.services.ICryptoService;
import sn.dialibah.dptracker.common.exceptions.InternalServerException;
import sn.dialibah.dptracker.common.models.Profile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * by osow on 15/11/17.
 * for kiss-api
 */
@Slf4j
@Service
public class AuthenticationService implements IAuthenticationService {

	private static final String LOG_HEADER = "[AUTHENTICATION SERVICE]";
	private final ICryptoService cryptoService;
	private final ProfileRepository profileRepository;
	private final DozerBeanMapper mapper;

	@Value("${kiss.token.header.name}")
	private String tokenHeaderName;

	@Autowired
	public AuthenticationService(ICryptoService cryptoService, ProfileRepository profileRepository, DozerBeanMapper mapper) {
		this.cryptoService = cryptoService;
		this.profileRepository = profileRepository;
		this.mapper = mapper;
	}


	@Override
	public Profile signup(SignupDataBean signupDataBean) {
		if(StringUtils.isBlank(signupDataBean.getPassword()) || StringUtils.isBlank(signupDataBean.getConfirmPassword()) || !signupDataBean.getPassword()
						.equals(signupDataBean.getConfirmPassword())){
			log.error("{} Empty password or not same passwords | pwd : {} | confirmPwd {}", LOG_HEADER, signupDataBean.getPassword(), signupDataBean.getConfirmPassword());
			throw new InvalidPasswordException("Empty password or not same passwords");
		}
		Profile profile = Profile.builder()
						.login(signupDataBean.getLogin())
						.password(cryptoService.encryptPassword(signupDataBean.getPassword()))
						.firstName(signupDataBean.getFirstName())
						.lastName(signupDataBean.getLastName())
						.build();
		log.info("{} Adding profile {}", LOG_HEADER, profile);

		ProfileEntity profileEntity = this.mapper.map(profile, ProfileEntity.class);
		profileEntity.setCreationDateTime(LocalDateTime.now());
		profileEntity.setLastModificationDateTime(LocalDateTime.now());

		log.info("{} Saving profile entity {}", LOG_HEADER, profileEntity);

		ProfileEntity saved;
		try{
			saved = this.profileRepository.save(profileEntity);
		}catch(DataAccessException dae){
			log.error("{} Save of profile {} failed with this error {}", LOG_HEADER, profileEntity, dae.getMessage());
			throw new InternalServerException("Save of profile failed with this error : " + dae.getMessage());
		}
		return saved == null ? null : profile;
	}

	@PostConstruct
	public void signupRoot() {
		if(profileRepository.findByLogin("root@dialibah.com") != null)
			return;
		SignupDataBean sbd = SignupDataBean.builder().login("root@dialibah.com").password("Passer33").confirmPassword("Passer33").build();
		signup(sbd);
	}

	@Override
	public Profile login(LoginDataBean loginDataBean) {
		log.info("{} looking for login {}", LOG_HEADER, loginDataBean.getLogin());
		// TODO Login with Active Directory first
		Profile profile = localLogin(loginDataBean.getLogin(), loginDataBean.getPassword());
		if(profile == null)
			throw new UnableToLoginException("Bad credentials");
		return profile;
	}

	@Override
	public void setJsonWebTokenUsingProfile(Profile profile, HttpServletResponse response) {
		final String jwt = cryptoService.createTokenFromProfile(profile);
		cryptoService.persistTokenInDB(profile.getLogin(), jwt);
		response.setHeader(tokenHeaderName, jwt);
	}

	@Override
	public Optional<Profile> getProfile(String profileId) {
		ProfileEntity profileEntity = profileRepository.findByLogin(profileId);
		return profileEntity == null ? Optional.empty() : Optional.of(mapper.map(profileEntity, Profile.class));
	}

	@Override
	public List<ProfileEntity> getAllUsers() {
		return Lists.newArrayList(profileRepository.findAll());
	}

	/**
	 * Login from mongo repository
	 * return {@code null} if bad credentials
	 *
	 * @param login    profile login
	 * @param password profile password
	 *
	 * @return profile
	 */
	private Profile localLogin(String login, String password) {
		Profile profile = mapper.map(profileRepository.findByLogin(login), Profile.class);
		return this.cryptoService.checkPassword(password, profile.getPassword()) ? profile : null;
	}

}
