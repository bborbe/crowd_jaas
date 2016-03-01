package de.benjaminborbe.jaas.crowd;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RestService {

  private static final Logger log = Logger.getLogger(RestService.class.getName());

  private final String crowdBaseUrl;

  private final String applicationName;

  private final String applicationPassword;

  public RestService(final String crowdBaseUrl, final String applicationName, final String applicationPassword) {
    this.crowdBaseUrl = crowdBaseUrl;
    this.applicationName = applicationName;
    this.applicationPassword = applicationPassword;
  }

  // https://developer.atlassian.com/display/CROWDDEV/Crowd+REST+Resources

  public boolean verifyLogin(final String username, final char[] password) {
    final StringBuffer sb = new StringBuffer();
    sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    sb.append("<password>");
    sb.append("<value>");
    sb.append(password);
    sb.append("</value>");
    sb.append("</password>");
    final Response returnCode = post(this.crowdBaseUrl + "rest/usermanagement/latest/authentication?username=" + username, sb.toString());
    log.log(Level.INFO, "login returncode = " + returnCode);
    return returnCode.isSuccess();
  }

  private Response post(final String url, final String content) {
    log.log(Level.INFO, "post url: " + url);
    try {
      final String type = "text/xml";
      final HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
      conn.setRequestProperty("Authorization", "Basic " + basicAuth(applicationName, applicationPassword));
      conn.setDoOutput(true);
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", type);
      conn.setRequestProperty("Content-Length", String.valueOf(content.length()));
      final OutputStream os = conn.getOutputStream();
      os.write(content.getBytes());
      return new Response(conn.getResponseCode());
    } catch (final IOException e) {
      log.log(Level.INFO, "post failed", e);
      return new Response(500);
    }
  }

  private String basicAuth(final String username, final String password) {
    final Base64.Encoder encoder = Base64.getEncoder();
    final String content = username + ":" + password;
    return encoder.encodeToString(content.getBytes());
  }

  public List<String> getGroups(final String username) {
    final Response response = get(this.crowdBaseUrl + "rest/usermanagement/latest/user/group/direct?username=" + username);
    if (!response.isSuccess()) {
      return Collections.emptyList();
    }
    final String content = new String(response.getContent());
    log.log(Level.INFO, content);
    return parseGroups(content);
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

  private Response get(final String url) {
    log.log(Level.INFO, "get url: " + url);
    try {
      final HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
      conn.setRequestProperty("Authorization", "Basic " + basicAuth(applicationName, applicationPassword));
      conn.setDoOutput(true);
      conn.setRequestMethod("GET");
      final InputStream is = conn.getInputStream();
      final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      int nRead;
      final byte[] data = new byte[16384];

      while ((nRead = is.read(data, 0, data.length)) != -1) {
        buffer.write(data, 0, nRead);
      }
      buffer.flush();
      return new Response(conn.getResponseCode(), buffer.toByteArray());
    } catch (final IOException e) {
      log.log(Level.INFO, "get failed", e);
      return new Response(500);
    }
  }

  private class Response {

    private final int responseCode;

    private final byte[] content;

    public Response(final int responseCode) {
      this.responseCode = responseCode;
      this.content = new byte[0];
    }

    public byte[] getContent() {
      return content;
    }

    public int getResponseCode() {
      return responseCode;
    }

    public Response(final int responseCode, final byte[] content) {
      this.responseCode = responseCode;
      this.content = content;
    }

    public boolean isSuccess() {
      return responseCode / 100 == 2;
    }
  }
}
