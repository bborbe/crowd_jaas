package de.benjaminborbe.jaas.crowd;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;

import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(InternalTest.class)
public class CrowdLoginModuleIntegrationTest {

  @Test
  public void testLogin() throws Exception {
    final String username = "bborbe";
    final char[] password = "test123".toCharArray();
    final CrowdLoginModule crowdLoginModule = new CrowdLoginModule();
    final CallbackHandler callbackHandler = new DummyCallbackHandler(username, password);
    final Subject subject = new Subject();
    final Map<String, String> sharedState = new HashMap<>();
    final Map<String, String> options = new HashMap<>();
    options.put(CrowdLoginModule.APPLICATION_NAME, "tomcat");
    options.put(CrowdLoginModule.APPLICATION_PASSWORD, "8Fvar50L");
    options.put(CrowdLoginModule.CROWD_SERVER_URL, "http://playground.hm.benjamin-borbe.de/crowd/");
    crowdLoginModule.initialize(subject, callbackHandler, sharedState, options);
    assertThat(crowdLoginModule.login(), is(true));
  }
}