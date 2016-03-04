package de.benjaminborbe.jaas.crowd;

import java.security.Principal;

public class RolePrincipal implements Principal {

  private final String name;

  public RolePrincipal(final String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

}
