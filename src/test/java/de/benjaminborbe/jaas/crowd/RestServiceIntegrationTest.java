package de.benjaminborbe.jaas.crowd;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(InternalTest.class)
public class RestServiceIntegrationTest {

  private final HttpService httpService = new HttpService();

  @Test
  public void testVerifyLogin() throws Exception {
    final CrowdConfig crowdConfig = new CrowdConfig("http://test.hm.benjamin-borbe.de/crowd/", "tomcat", "8Fvar50L");
    final RestService restService = new RestService(httpService);
    assertThat(restService.verifyLogin(crowdConfig, "bborbe", "test123".toCharArray()), is(true));
  }

  @Test
  public void testVerifyLoginServerPasswordWrong() throws Exception {
    final CrowdConfig crowdConfig = new CrowdConfig("http://test.hm.benjamin-borbe.de/crowd/", "tomcat", "foo");
    final RestService restService = new RestService(httpService);
    assertThat(restService.verifyLogin(crowdConfig, "bborbe", "test123".toCharArray()), is(false));
  }

  @Test
  public void testVerifyLoginPasswordWrong() throws Exception {
    final CrowdConfig crowdConfig = new CrowdConfig("http://test.hm.benjamin-borbe.de/crowd/", "tomcat", "8Fvar50L");
    final RestService restService = new RestService(httpService);
    assertThat(restService.verifyLogin(crowdConfig, "bborbe", "foo".toCharArray()), is(false));
  }

  @Test
  public void testGetGroups() throws Exception {
    final CrowdConfig crowdConfig = new CrowdConfig("http://test.hm.benjamin-borbe.de/crowd/", "tomcat", "8Fvar50L");
    final RestService restService = new RestService(httpService);
    restService.getGroups(crowdConfig, "bborbe");
  }

}