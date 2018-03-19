package sn.dialibah.kiss.webapi.authentication;

import org.junit.After;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import sn.dialibah.kiss.webapi.ApplicationTest;
import sn.dialibah.kiss.webapi.authentication.exceptions.InvalidPasswordException;
import sn.dialibah.kiss.webapi.authentication.exceptions.UnableToLoginException;
import sn.dialibah.kiss.webapi.authentication.models.LoginDataBean;
import sn.dialibah.kiss.webapi.authentication.models.SignupDataBean;
import sn.dialibah.kiss.webapi.authentication.repositories.ProfileRepository;
import sn.dialibah.kiss.webapi.authentication.services.AuthenticationService;
import sn.dialibah.kiss.webapi.common.exceptions.InternalServerException;
import sn.dialibah.kiss.webapi.common.models.Profile;

/**
 * by osow on 15/11/17.
 * for kiss-api
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class AuthenticationTest {

	@Autowired
	private AuthenticationService authenticationService;
	@Autowired
	private ProfileRepository profileRepository;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testSignup() {
		SignupDataBean signupDataBean = SignupDataBean.builder()
						.login("kiss")
						.password("Passer33")
						.confirmPassword("Passer33")
						.firstName("Ousmane Sow")
						.build();
		Profile retrievedProfile = this.authenticationService.signup(signupDataBean);
		Assert.assertNotNull(retrievedProfile);
		Assert.assertEquals(retrievedProfile.getLogin(), "kiss");
		Assert.assertEquals(retrievedProfile.getLogin(), "kiss");
	}

	@Test
	public void testDuplicatedUser() throws InternalServerException {
		SignupDataBean signupDataBean = SignupDataBean.builder()
						.login("kiss")
						.firstName("Ousmane Sow")
						.password("Passer33")
						.confirmPassword("Passer33")
						.build();
		Profile retrievedProfile = this.authenticationService.signup(signupDataBean);
		Assert.assertNotNull(retrievedProfile);
		thrown.expect(InternalServerException.class);
		retrievedProfile = this.authenticationService.signup(signupDataBean);
		Assert.assertNull(retrievedProfile);
	}

	@Test
	public void testBothPasswords() throws InvalidPasswordException {
		thrown.expect(InvalidPasswordException.class);

		SignupDataBean signupDataBean = SignupDataBean.builder()
						.login("osow")
						.firstName("Ousmane SOW")
						.confirmPassword("test")
						.build();
		Profile retrievedProfile = this.authenticationService.signup(signupDataBean);
		Assert.assertNull(retrievedProfile);

		signupDataBean.setPassword("wrong pwd");
		retrievedProfile = this.authenticationService.signup(signupDataBean);
		Assert.assertNull(retrievedProfile);

		signupDataBean.setPassword("test");
		retrievedProfile = this.authenticationService.signup(signupDataBean);
		Assert.assertNotNull(retrievedProfile);
	}

	@Test
	public void testLoginMongo() {
		SignupDataBean signupDataBean = SignupDataBean.builder()
						.login("kissMongoUser")
						.password("mongo")
						.confirmPassword("mongo")
						.firstName("First Name")
						.build();
		Profile savedProfile = this.authenticationService.signup(signupDataBean);
		Profile loggedInProfile = null;
		UnableToLoginException exceptedException = null;
		try {
			loggedInProfile = this.authenticationService.login(LoginDataBean.builder()
							.login("kissMongoUser")
							.password("badPassword")
							.build());
		} catch (UnableToLoginException le) {
			exceptedException = le;
		}
		Assert.assertNotNull(exceptedException);
		Assert.assertNull(loggedInProfile);
		exceptedException = null;
		try {
			loggedInProfile = this.authenticationService.login(LoginDataBean.builder()
							.login("kissMongoUser")
							.password("mongo")
							.build());
		} catch (UnableToLoginException le) {
			exceptedException = le;
		}
		Assert.assertNull(exceptedException);
		Assert.assertNotNull(loggedInProfile);
		Assert.assertEquals(savedProfile.getFirstName(), loggedInProfile.getFirstName());

	}


	@After
	public void flushCollection() {
		this.profileRepository.deleteAll();
	}
}
