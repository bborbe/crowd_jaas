package de.benjaminborbe.jaas.crowd;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(InternalTest.class)
public class CrowdLoginModuleIntegrationTest {

  @Test
  public void testLoginSuccess() throws Exception {
    final String username = "sample";
    final char[] password = "cvwRmFJt".toCharArray();
    final CrowdLoginModule crowdLoginModule = new CrowdLoginModule();
    final CallbackHandler callbackHandler = new DummyCallbackHandler(username, password);
    final Subject subject = new Subject();
    final Map<String, String> sharedState = new HashMap<>();
    final Map<String, String> options = new HashMap<>();
    options.put(CrowdLoginModule.APPLICATION_NAME, "sample");
    options.put(CrowdLoginModule.APPLICATION_PASSWORD, "in4402xD");
    options.put(CrowdLoginModule.CROWD_SERVER_URL, "https://template.codeyard.com/crowd/");
    crowdLoginModule.initialize(subject, callbackHandler, sharedState, options);
    assertThat(crowdLoginModule.login(), is(true));
  }

  @Test(expected = LoginException.class)
  public void testLoginFailed() throws Exception {
    final String username = "sample";
    final char[] password = "foo".toCharArray();
    final CrowdLoginModule crowdLoginModule = new CrowdLoginModule();
    final CallbackHandler callbackHandler = new DummyCallbackHandler(username, password);
    final Subject subject = new Subject();
    final Map<String, String> sharedState = new HashMap<>();
    final Map<String, String> options = new HashMap<>();
    options.put(CrowdLoginModule.APPLICATION_NAME, "sample");
    options.put(CrowdLoginModule.APPLICATION_PASSWORD, "in4402xD");
    options.put(CrowdLoginModule.CROWD_SERVER_URL, "https://template.codeyard.com/crowd/");
    crowdLoginModule.initialize(subject, callbackHandler, sharedState, options);
    crowdLoginModule.login();
  }
}