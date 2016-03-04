package de.benjaminborbe.jaas.crowd;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CrowdLoginModuleUnitTest {

  @Mock
  private RestService restService;

  @InjectMocks
  private CrowdLoginModule crowdLoginModule;

  @Test
  public void testLoginSuccess() throws Exception {
    final String username = "bborbe";
    final char[] password = "test123".toCharArray();
    when(restService.verifyLogin(any(CrowdConfig.class), eq(username), eq(password))).thenReturn(true);
    final CallbackHandler callbackHandler = new DummyCallbackHandler(username, password);
    final Subject subject = new Subject();
    final Map<String, String> sharedState = new HashMap<>();
    final Map<String, String> options = new HashMap<>();
    crowdLoginModule.initialize(subject, callbackHandler, sharedState, options);
    assertThat(crowdLoginModule.login(), is(true));
    assertThat(subject.getPrincipals().isEmpty(), is(true));
    crowdLoginModule.commit();
    assertThat(subject.getPrincipals().isEmpty(), is(false));
  }

  @Test
  public void testLogoutSuccess() throws Exception {
    final String username = "bborbe";
    final char[] password = "test123".toCharArray();
    when(restService.verifyLogin(any(CrowdConfig.class), eq(username), eq(password))).thenReturn(true);
    final CallbackHandler callbackHandler = new DummyCallbackHandler(username, password);
    final Subject subject = new Subject();
    final Map<String, String> sharedState = new HashMap<>();
    final Map<String, String> options = new HashMap<>();
    crowdLoginModule.initialize(subject, callbackHandler, sharedState, options);
    assertThat(crowdLoginModule.login(), is(true));
    assertThat(subject.getPrincipals().isEmpty(), is(true));
    crowdLoginModule.commit();
    assertThat(subject.getPrincipals().isEmpty(), is(false));
    crowdLoginModule.logout();
    assertThat(subject.getPrincipals().isEmpty(), is(true));
  }

  @Test(expected = LoginException.class)
  public void testLoginFail() throws Exception {
    final String username = "bborbe";
    final char[] password = "test123".toCharArray();
    when(restService.verifyLogin(any(CrowdConfig.class), eq(username), eq(password))).thenReturn(false);
    final CallbackHandler callbackHandler = new DummyCallbackHandler(username, password);
    final Subject subject = new Subject();
    final Map<String, String> sharedState = new HashMap<>();
    final Map<String, String> options = new HashMap<>();
    crowdLoginModule.initialize(subject, callbackHandler, sharedState, options);
    crowdLoginModule.login();
  }

}