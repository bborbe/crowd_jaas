package de.benjaminborbe.jaas.crowd;

import java.security.Principal;

public class UserPrincipal implements Principal {

  private final String name;

  public UserPrincipal(final String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

}
