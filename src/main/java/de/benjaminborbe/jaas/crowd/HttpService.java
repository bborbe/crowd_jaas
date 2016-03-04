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

  public HttpService() {
  }

  public Response post(final URL url, final String content, final String applicationName, final String applicationPassword) {
    LOGGER.log(Level.FINE, String.format("post url: %s", url));
    try {
      final String contentType = "text/xml";
      final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestProperty("Authorization", "Basic " + basicAuth(applicationName, applicationPassword));
      conn.setDoOutput(true);
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", contentType);
      conn.setRequestProperty("Content-Length", String.valueOf(content.length()));
      final OutputStream os = conn.getOutputStream();
      os.write(content.getBytes());
      return new Response(conn.getResponseCode());
    } catch (final IOException e) {
      LOGGER.log(Level.WARNING, String.format("post request to %s failed", url), e);
      return new Response(500);
    }
  }

  public Response get(final URL url, final String applicationName, final String applicationPassword) {
    LOGGER.log(Level.FINE, String.format("get url: %s", url));
    try {
      final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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
      LOGGER.log(Level.WARNING, String.format("get request to %s failed", url), e);
      return new Response(500);
    }
  }

  private String basicAuth(final String username, final String password) {
    final Base64.Encoder encoder = Base64.getEncoder();
    final String content = username + ":" + password;
    return encoder.encodeToString(content.getBytes());
  }

}
