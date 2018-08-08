package com.github.wovnio.wovnjava;

import junit.framework.TestCase;

import java.util.HashMap;
import org.easymock.EasyMock;

import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

public class WovnHttpServletResponseTest extends TestCase {

  public void testReplaceLocation() {
    WovnHttpServletResponse res = getLocationMock("/dir/page", "redirect?wovn=en");
    assertEquals("/dir/redirect?wovn=en", res.getHeader("Location"));
  }

  private WovnHttpServletResponse getLocationMock(String path, String location) {
    HttpServletRequest req = TestUtil.mockRequestPath(path);
    Settings s = TestUtil.makeSettings();
    Headers h = new Headers(req, s);
    HttpServletResponseMock response = new HttpServletResponseMock();
    response.addHeader("Location", location);
    return new WovnHttpServletResponse((HttpServletResponse)response, h);
  }
}

