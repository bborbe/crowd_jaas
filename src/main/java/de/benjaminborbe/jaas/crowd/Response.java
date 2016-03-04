package de.benjaminborbe.jaas.crowd;

public class Response {

  private final int responseCode;

  private final byte[] content;

  public Response(final int responseCode) {
    this.responseCode = responseCode;
    this.content = new byte[0];
  }

  public Response(final int responseCode, final byte[] content) {
    this.responseCode = responseCode;
    this.content = content;
  }

  public byte[] getContent() {
    return content;
  }

  public int getResponseCode() {
    return responseCode;
  }

  public boolean isSuccess() {
    return responseCode / 100 == 2;
  }
}
