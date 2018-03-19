package sn.dialibah.kiss.webapi.authentication;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import sn.dialibah.kiss.webapi.ApplicationTest;
import sn.dialibah.kiss.webapi.common.configurations.security.models.TokenWrapper;
import sn.dialibah.kiss.webapi.common.configurations.security.services.ICryptoService;
import sn.dialibah.kiss.webapi.common.models.Profile;

import java.time.LocalDateTime;

/**
 * by osow on 15/11/17.
 * for kiss-api
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class CryptoServiceTest {

	@Autowired
	private ICryptoService cryptoService;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testPasswordEncryption() {
		String password = "myPassword";
		String encryptedPassword = cryptoService.encryptPassword(password);
		Assert.assertNotEquals(password, encryptedPassword);
		Assert.assertTrue(cryptoService.checkPassword(password, encryptedPassword));
		Assert.assertFalse(cryptoService.checkPassword(password + "toto", encryptedPassword));
	}

	@Test
	public void testJWTCreationFromProfile() {
		String jwt = cryptoService.createTokenFromProfile(Profile.builder()
						.login("test")
						.password("test")
						.build());
		Assert.assertTrue(StringUtils.isNotBlank(jwt));
	}

	@Test
	public void testPersistTokenInDB() {
		thrown.expect(DuplicateKeyException.class);

		Profile p = Profile.builder().login("test").password("test").build();
		String jwt = cryptoService.createTokenFromProfile(p);
		Long numberOfToken = cryptoService.persistTokenInDB(p.getLogin(), jwt);
		Assert.assertTrue(numberOfToken == 1);
		numberOfToken = cryptoService.persistTokenInDB(p.getLogin(), jwt);
		Assert.assertTrue(numberOfToken == 1);
	}

	@Test
	public void testTokenWrapping() {
		Profile p = Profile.builder().login("test2").password("test").build();
		String jwt = cryptoService.createTokenFromProfile(p);
		cryptoService.persistTokenInDB(p.getLogin(), jwt);
		TokenWrapper tokenWrapper = cryptoService.parseUserProfileFromToken(jwt);
		Assert.assertEquals(tokenWrapper.getProfileId(), p.getLogin());
		Assert.assertTrue(StringUtils.isNotBlank(tokenWrapper.getExpirationDate()));
		Assert.assertTrue(LocalDateTime.parse(tokenWrapper.getExpirationDate()).isAfter(LocalDateTime.now()));
	}

}
