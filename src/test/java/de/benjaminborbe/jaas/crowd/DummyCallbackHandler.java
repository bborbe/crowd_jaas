package de.benjaminborbe.jaas.crowd;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

public class DummyCallbackHandler implements CallbackHandler {

  private String username;

  private char[] password;

  public DummyCallbackHandler(final String username, final char[] password) {
    this.password = password;
    this.username = username;
  }

  @Override
  public void handle(final Callback[] callbacks) throws IOException, UnsupportedCallbackException {
    final NameCallback nameCallback = (NameCallback) callbacks[0];
    final PasswordCallback passwordCallback = (PasswordCallback) callbacks[1];
    nameCallback.setName(username);
    passwordCallback.setPassword(password);
  }
}
