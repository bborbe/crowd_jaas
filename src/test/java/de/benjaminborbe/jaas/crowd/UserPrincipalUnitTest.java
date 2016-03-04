package de.benjaminborbe.jaas.crowd;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class UserPrincipalUnitTest {

  @Test
  public void testName() throws Exception {
    final String name = "role";
    final UserPrincipal principal = new UserPrincipal(name);
    assertThat(principal.getName(), is(name));
  }
}