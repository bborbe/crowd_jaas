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
    final CrowdConfig crowdConfig = new CrowdConfig("https://template.codeyard.com/crowd/", "sample", "in4402xD");
    final RestService restService = new RestService(httpService);
    assertThat(restService.verifyLogin(crowdConfig, "sample", "cvwRmFJt".toCharArray()), is(true));
  }

  @Test
  public void testVerifyLoginServerPasswordWrong() throws Exception {
    final CrowdConfig crowdConfig = new CrowdConfig("https://template.codeyard.com/crowd/", "sample", "foo");
    final RestService restService = new RestService(httpService);
    assertThat(restService.verifyLogin(crowdConfig, "sample", "cvwRmFJt".toCharArray()), is(false));
  }

  @Test
  public void testVerifyLoginPasswordWrong() throws Exception {
    final CrowdConfig crowdConfig = new CrowdConfig("https://template.codeyard.com/crowd/", "sample", "in4402xD");
    final RestService restService = new RestService(httpService);
    assertThat(restService.verifyLogin(crowdConfig, "sample", "foo".toCharArray()), is(false));
  }

  @Test
  public void testGetGroups() throws Exception {
    final CrowdConfig crowdConfig = new CrowdConfig("https://template.codeyard.com/crowd/", "sample", "in4402xD");
    final RestService restService = new RestService(httpService);
    restService.getGroups(crowdConfig, "sample");
  }

}