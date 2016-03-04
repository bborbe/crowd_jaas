package de.benjaminborbe.jaas.crowd;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

// https://developer.atlassian.com/display/CROWDDEV/Crowd+REST+Resources
public class RestService {

  private static final Logger LOGGER = Logger.getLogger(RestService.class.getName());

  private final HttpService httpService;

  public RestService(final HttpService httpService) {
    this.httpService = httpService;
  }

  public boolean verifyLogin(final CrowdConfig crowdConfig, final String username, final char[] password) throws MalformedURLException {
    final StringBuffer content = new StringBuffer();
    content.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    content.append("<password>");
    content.append("<value>");
    content.append(password);
    content.append("</value>");
    content.append("</password>");
    final Response returnCode = httpService.post(
        buildUrl(crowdConfig.getCrowdBaseUrl() + "rest/usermanagement/latest/authentication?username=" + username),
        content.toString(), crowdConfig.getApplicationName(), crowdConfig.getApplicationPassword());
    final boolean success = returnCode.isSuccess();
    LOGGER.log(Level.FINE, String.format("login for user %s %s", username, success ? "success" : "fail"));
    return success;
  }

  private URL buildUrl(final String url) throws MalformedURLException {
    return new URL(url);
  }

  public List<String> getGroups(final CrowdConfig crowdConfig, final String username) throws MalformedURLException {
    final Response response = httpService.get(
        buildUrl(crowdConfig.getCrowdBaseUrl() + "rest/usermanagement/latest/user/group/direct?username=" + username),
        crowdConfig.getApplicationName(), crowdConfig.getApplicationPassword());
    if (!response.isSuccess()) {
      return Collections.emptyList();
    }
    final String content = new String(response.getContent());
    final List<String> groups = parseGroups(content);
    LOGGER.log(Level.FINE, String.format("groups for user %s %s", username, groups));
    return groups;
  }

  protected static List<String> parseGroups(final String content) {
    final List<String> result = new ArrayList<>();
    int start = 0;
    while (true) {
      final int groupPos = content.indexOf("<group", start);
      if (groupPos == -1) {
        return result;
      }
      final int namePos = content.indexOf("name=", groupPos);
      if (namePos == -1) {
        return result;
      }
      final int endPos = content.indexOf("\"", namePos + 6);
      if (endPos == -1) {
        return result;
      }
      result.add(content.substring(namePos + 6, endPos));
      start = endPos;
    }
  }

}
