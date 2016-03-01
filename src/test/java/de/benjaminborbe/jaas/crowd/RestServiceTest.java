package de.benjaminborbe.jaas.crowd;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Ignore;
import org.junit.Test;

public class RestServiceTest {

  @Ignore("need crowd server")
  @Test
  public void testVerifyLogin() throws Exception {
    final RestService restService = new RestService("http://test.hm.benjamin-borbe.de/crowd/", "tomcat", "8Fvar50L");
    assertThat(restService.verifyLogin("bborbe", "test123".toCharArray()), is(true));
  }

  @Ignore("need crowd server")
  @Test
  public void testVerifyLoginServerPasswordWrong() throws Exception {
    final RestService restService = new RestService("http://test.hm.benjamin-borbe.de/crowd/", "tomcat", "foo");
    assertThat(restService.verifyLogin("bborbe", "test123".toCharArray()), is(false));
  }

  @Ignore("need crowd server")
  @Test
  public void testVerifyLoginPasswordWrong() throws Exception {
    final RestService restService = new RestService("http://test.hm.benjamin-borbe.de/crowd/", "tomcat", "8Fvar50L");
    assertThat(restService.verifyLogin("bborbe", "foo".toCharArray()), is(false));
  }

  @Ignore("need crowd server")
  @Test
  public void testGetGroups() throws Exception {
    final RestService restService = new RestService("http://test.hm.benjamin-borbe.de/crowd/", "tomcat", "8Fvar50L");
    restService.getGroups("bborbe");
  }

  @Test
  public void testParseGroup() throws Exception {
    final String content = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><groups><group name=\"administrators\"></group></groups>";
    assertThat(RestService.parseGroups(content), notNullValue());
    assertThat(RestService.parseGroups(content).size(), is(1));
    assertThat(RestService.parseGroups(content).get(0), is("administrators"));
  }

  @Test
  public void testParseGroups() throws Exception {
    final String content = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><groups expand=\"group\"><group name=\"crowd-administrators\"><link href=\"http://test.hm.benjamin-borbe.de/crowd/rest/usermanagement/latest/group?groupname=crowd-administrators\" rel=\"self\"/></group><group name=\"test-administrators\"><link href=\"http://test.hm.benjamin-borbe.de/crowd/rest/usermanagement/latest/group?groupname=test-administrators\" rel=\"self\"/></group><group name=\"test-users\"><link href=\"http://test.hm.benjamin-borbe.de/crowd/rest/usermanagement/latest/group?groupname=test-users\" rel=\"self\"/></group><group name=\"sonar-administrators\"><link href=\"http://test.hm.benjamin-borbe.de/crowd/rest/usermanagement/latest/group?groupname=sonar-administrators\" rel=\"self\"/></group><group name=\"sonar-users\"><link href=\"http://test.hm.benjamin-borbe.de/crowd/rest/usermanagement/latest/group?groupname=sonar-users\" rel=\"self\"/></group></groups>";
    assertThat(RestService.parseGroups(content), notNullValue());
    assertThat(RestService.parseGroups(content).size(), is(5));
    assertThat(RestService.parseGroups(content).get(0), is("crowd-administrators"));
    assertThat(RestService.parseGroups(content).get(1), is("test-administrators"));
    assertThat(RestService.parseGroups(content).get(2), is("test-users"));
    assertThat(RestService.parseGroups(content).get(3), is("sonar-administrators"));
    assertThat(RestService.parseGroups(content).get(4), is("sonar-users"));
  }
}