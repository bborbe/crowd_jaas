package de.benjaminborbe.jaas.crowd;

import java.io.IOException;
import java.util.ArrayList;
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

  private static final Logger log = Logger.getLogger(CrowdLoginModule.class.getName());

  private CallbackHandler handler;

  private Subject subject;

  private UserPrincipal userPrincipal;

  private RolePrincipal rolePrincipal;

  private String login;

  private List<String> userGroups;

  private RestService restService;

  @Override
  public void initialize(final Subject subject, final CallbackHandler callbackHandler, final Map<String, ?> sharedState,
      final Map<String, ?> options) {
    this.handler = callbackHandler;
    this.subject = subject;
    final String name = getString(options, "application.name");
    final String url = getString(options, "crowd.base.url");
    final String password = getString(options, "application.password");
    restService = new RestService(url, name, password);
  }

  private String getString(final Map<String, ?> options, final String key) {
    return String.valueOf(options.get(key));
  }

  @Override
  public boolean login() throws LoginException {

    final Callback[] callbacks = new Callback[2];
    callbacks[0] = new NameCallback("login");
    callbacks[1] = new PasswordCallback("password", true);

    try {
      handler.handle(callbacks);
      final NameCallback nameCallback = (NameCallback) callbacks[0];
      final PasswordCallback passwordCallback = (PasswordCallback) callbacks[1];
      final String name = nameCallback.getName();
      final char[] password = passwordCallback.getPassword();
      log.log(Level.INFO, "name: " + name + " password: " + String.valueOf(password));
      if (restService.verifyLogin(name, password)) {
        login = name;
        userGroups = restService.getGroups(name);
        return true;
      }

      // If credentials are NOT OK we throw a LoginException
      throw new LoginException("Authentication failed");

    } catch (final IOException | UnsupportedCallbackException e) {
      throw new LoginException(e.getMessage());
    }

  }

  @Override
  public boolean commit() throws LoginException {

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
    return false;
  }

  @Override
  public boolean logout() throws LoginException {
    subject.getPrincipals().remove(userPrincipal);
    subject.getPrincipals().remove(rolePrincipal);
    return true;
  }

}
