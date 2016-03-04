package de.benjaminborbe.jaas.crowd;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class RolePrincipalUnitTest {

  @Test
  public void testName() throws Exception {
    final String name = "role";
    final RolePrincipal principal = new RolePrincipal(name);
    assertThat(principal.getName(), is(name));
  }
}