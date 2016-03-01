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

  @Override
  public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
    handler = callbackHandler;
    this.subject = subject;
  }

  @Override
  public boolean login() throws LoginException {

    Callback[] callbacks = new Callback[2];
    callbacks[0] = new NameCallback("login");
    callbacks[1] = new PasswordCallback("password", true);

    try {
      handler.handle(callbacks);
      final NameCallback nameCallback = (NameCallback) callbacks[0];
      final PasswordCallback passwordCallback = (PasswordCallback) callbacks[1];
      String name = nameCallback.getName();
      String password = String.valueOf(passwordCallback.getPassword());
      log.log(Level.INFO, "name: " + name + " password: " + password);
      if (name != null && name.equals("bborbe") && password != null
          && password.equals("test123")) {
        login = name;
        userGroups = new ArrayList<>();
        userGroups.add("manager-gui");
        return true;
      }

      // If credentials are NOT OK we throw a LoginException
      throw new LoginException("Authentication failed");

    } catch (IOException e) {
      throw new LoginException(e.getMessage());
    } catch (UnsupportedCallbackException e) {
      throw new LoginException(e.getMessage());
    }

  }

  @Override
  public boolean commit() throws LoginException {

    userPrincipal = new UserPrincipal(login);
    subject.getPrincipals().add(userPrincipal);

    if (userGroups != null && userGroups.size() > 0) {
      for (String groupName : userGroups) {
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
