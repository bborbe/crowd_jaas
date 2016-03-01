package de.benjaminborbe.jaas.crowd;

import java.security.Principal;

public class RolePrincipal implements Principal {
	
	private String name;
	
  public RolePrincipal(final String name) {
		super();
		this.name = name;
	}

  public void setName(final String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

}
