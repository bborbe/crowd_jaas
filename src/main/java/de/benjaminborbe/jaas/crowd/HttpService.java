package de.benjaminborbe.jaas.crowd;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpService {

	private static final Logger LOGGER = Logger.getLogger(HttpService.class.getName());

	private static final String CONTENT_TYPE = "application/xml";

	public HttpService() {
  }

  public Response post(final URL url, final String body, final String applicationName, final String applicationPassword) {
    LOGGER.log(Level.INFO, String.format("post url: %s body: %s", url, body));
    try {
		final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestProperty("Authorization", "Basic " + basicAuth(applicationName, applicationPassword));
      conn.setDoOutput(true);
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", CONTENT_TYPE);
      conn.setRequestProperty("Content-Length", String.valueOf(body.length()));
      final OutputStream os = conn.getOutputStream();
      os.write(body.getBytes());
      if (conn.getResponseCode() / 100 != 2) {
        return new Response(conn.getResponseCode());
      }
      final byte[] responseContent = getBytes(conn);
      LOGGER.log(Level.INFO, "response: %s", new String(responseContent));
      return new Response(conn.getResponseCode(), responseContent);
    } catch (final IOException e) {
      LOGGER.log(Level.WARNING, String.format("post request to %s failed", url), e);
      return new Response(500);
    }
  }

  public Response get(final URL url, final String applicationName, final String applicationPassword) {
    LOGGER.log(Level.INFO, String.format("get url: %s", url));
    try {
      final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestProperty("Authorization", "Basic " + basicAuth(applicationName, applicationPassword));
      conn.setDoOutput(true);
      conn.setRequestMethod("GET");
      if (conn.getResponseCode() / 100 != 2) {
        return new Response(conn.getResponseCode());
      }
      final byte[] responseContent = getBytes(conn);
      LOGGER.log(Level.INFO, "response: %s", new String(responseContent));
      return new Response(conn.getResponseCode(), responseContent);
    } catch (final IOException e) {
      LOGGER.log(Level.WARNING, String.format("get request to %s failed", url), e);
      return new Response(500);
    }
  }

  private byte[] getBytes(final HttpURLConnection conn) throws IOException {
    final InputStream is = conn.getInputStream();
    final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    int nRead;
    final byte[] data = new byte[16384];

    while ((nRead = is.read(data, 0, data.length)) != -1) {
      buffer.write(data, 0, nRead);
    }
    buffer.flush();
    return buffer.toByteArray();
  }

  private String basicAuth(final String username, final String password) {
    LOGGER.log(Level.INFO, String.format("auth %s:%s", username, password));
    final Base64.Encoder encoder = Base64.getEncoder();
    final String content = username + ":" + password;
    return encoder.encodeToString(content.getBytes());
  }

}
