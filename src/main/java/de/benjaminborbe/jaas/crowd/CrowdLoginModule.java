package de.benjaminborbe.jaas.crowd;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

public class CrowdLoginModule implements LoginModule {

  private static final Logger LOGGER = Logger.getLogger(CrowdLoginModule.class.getName());

  private CallbackHandler handler;

  private Subject subject;

  private UserPrincipal userPrincipal;

  private RolePrincipal rolePrincipal;

  private String login;

  private List<String> userGroups;

  private RestService restService;

  private CrowdConfig crowdConfig;

  @Override
  public void initialize(final Subject subject, final CallbackHandler callbackHandler, final Map<String, ?> sharedState,
      final Map<String, ?> options) {
    this.handler = callbackHandler;
    this.subject = subject;
    this.crowdConfig = createCrowdConfig(options);
    this.restService = new RestService(new HttpService());
  }

  private CrowdConfig createCrowdConfig(final Map<String, ?> options) {
    final String applicationName = getString(options, "application.applicationName");
    final String crowdBaseUrl = getString(options, "crowd.base.crowdBaseUrl");
    final String applicationPassword = getString(options, "application.applicationPassword");
    LOGGER.log(Level.FINE, String.format("crowd base crowdBaseUrl %s", crowdBaseUrl));
    LOGGER.log(Level.FINE, String.format("crowd application applicationName %s", applicationName));
    LOGGER.log(Level.FINE, String.format("crowd application applicationPassword length %d", applicationPassword.length()));
    return new CrowdConfig(crowdBaseUrl, applicationName, applicationPassword);
  }

  private String getString(final Map<String, ?> options, final String key) {
    return String.valueOf(options.get(key));
  }

  @Override
  public boolean login() throws LoginException {
    try {
      LOGGER.log(Level.FINE, "login");
      final Callback[] callbacks = new Callback[2];
      callbacks[0] = new NameCallback("login");
      callbacks[1] = new PasswordCallback("applicationPassword", true);

      handler.handle(callbacks);
      final NameCallback nameCallback = (NameCallback) callbacks[0];
      final PasswordCallback passwordCallback = (PasswordCallback) callbacks[1];
      final String name = nameCallback.getName();
      final char[] password = passwordCallback.getPassword();
      LOGGER.log(Level.FINE, String.format("applicationName: %s applicationPassword-length: %d", name, password.length));
      if (restService.verifyLogin(crowdConfig, name, password)) {
        login = name;
        userGroups = restService.getGroups(crowdConfig, name);
        return true;
      }
      // If credentials are NOT OK we throw a LoginException
      throw new LoginException("Authentication failed");
    } catch (final IOException | UnsupportedCallbackException e) {
      LOGGER.log(Level.WARNING, "verify login failed", e);
      throw new LoginException(e.getMessage());
    }
  }

  @Override
  public boolean commit() throws LoginException {
    LOGGER.log(Level.FINE, "commit");

    userPrincipal = new UserPrincipal(login);
    subject.getPrincipals().add(userPrincipal);

    if (userGroups != null && userGroups.size() > 0) {
      for (final String groupName : userGroups) {
        rolePrincipal = new RolePrincipal(groupName);
        subject.getPrincipals().add(rolePrincipal);
      }
    }

    return true;
  }

  @Override
  public boolean abort() throws LoginException {
    LOGGER.log(Level.FINE, "abort");
    return false;
  }

  @Override
  public boolean logout() throws LoginException {
    LOGGER.log(Level.FINE, "logout");
    subject.getPrincipals().remove(userPrincipal);
    subject.getPrincipals().remove(rolePrincipal);
    return true;
  }

}
