import java.io.IOException;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.junit.Test;

import de.benjaminborbe.jaas.crowd.CrowdLoginModule;

public class CrowdLoginModuleTest {

  @Test
  public void testLogin() throws Exception {
    CrowdLoginModule crowdLoginModule = new CrowdLoginModule();
    Subject subject = null;
    CallbackHandler callbackHandler = new CallbackHandler() {

      @Override
      public void handle(final Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        final NameCallback nameCallback = (NameCallback) callbacks[0];
        final PasswordCallback passwordCallback = (PasswordCallback) callbacks[1];
        nameCallback.setName("bborbe");
        passwordCallback.setPassword("test123".toCharArray());
      }
    };
    Map<String, ?> sharedState = null;
    Map<String, ?> options = null;
    crowdLoginModule.initialize(subject, callbackHandler, sharedState, options);
                          crowdLoginModule.login();
  }
}