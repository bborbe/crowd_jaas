package de.benjaminborbe.jaas.crowd;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(InternalTest.class)
public class CrowdLoginModuleIntegrationTest {

  @Test
  public void testLogin() throws Exception {
    final CrowdLoginModule crowdLoginModule = new CrowdLoginModule();
    final Subject subject = null;
    final CallbackHandler callbackHandler = new CallbackHandler() {

      @Override
      public void handle(final Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        final NameCallback nameCallback = (NameCallback) callbacks[0];
        final PasswordCallback passwordCallback = (PasswordCallback) callbacks[1];
        nameCallback.setName("bborbe");
        passwordCallback.setPassword("test123".toCharArray());
      }
    };
    final Map<String, String> sharedState = new HashMap<>();
    final Map<String, String> options = new HashMap<>();
    options.put("application.name", "tomcat");
    options.put("application.password", "8Fvar50L");
    options.put("crowd.base.url", "http://playground.hm.benjamin-borbe.de/crowd/");
    crowdLoginModule.initialize(subject, callbackHandler, sharedState, options);
    assertThat(crowdLoginModule.login(), is(true));
  }
}