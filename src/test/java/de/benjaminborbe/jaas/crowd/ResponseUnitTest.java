package de.benjaminborbe.jaas.crowd;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ResponseUnitTest {

  @Test
  public void testGetContent() throws Exception {
    {
      final Response response = new Response(200);
      assertThat(response.getContent().length, is(0));
    }
    {
      final byte[] content = "hello".getBytes();
      final Response response = new Response(200, content);
      assertThat(response.getContent(), is(content));
    }
  }

  @Test
  public void testGetResponseCode() throws Exception {
    {
      final int value = 200;
      final Response response = new Response(value);
      assertThat(response.getResponseCode(), is(value));
    }
    {
      final int value = 200;
      final Response response = new Response(value, "hello".getBytes());
      assertThat(response.getResponseCode(), is(value));
    }
  }

  @Test
  public void testIsSuccess() throws Exception {
    {
      final int value = 200;
      final Response response = new Response(value);
      assertThat(response.isSuccess(), is(true));
    }
    {
      final int value = 200;
      final Response response = new Response(value, "hello".getBytes());
      assertThat(response.isSuccess(), is(true));
    }
    {
      final int value = 300;
      final Response response = new Response(value);
      assertThat(response.isSuccess(), is(false));
    }
    {
      final int value = 300;
      final Response response = new Response(value, "hello".getBytes());
      assertThat(response.isSuccess(), is(false));
    }
  }
}