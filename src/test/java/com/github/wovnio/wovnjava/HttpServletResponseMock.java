package com.github.wovnio.wovnjava;

import java.util.HashMap;
import java.util.ArrayList;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.servlet.http.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

class HttpServletResponseMock implements HttpServletResponse {
  public int status = 200;
  public String uri = "";
  public String path = "";
  public String query = "";
  public String contextPath = "";
  public String charset = "";
  public HashMap<String, String> headers = new HashMap<String, String>();

  public void addCookie(Cookie cookie) {
  }

  public boolean containsHeader(java.lang.String name) {
    return headers.get(name) != null;
  }

  public java.lang.String encodeURL(java.lang.String url) {
    return url;
  }

  public java.lang.String encodeRedirectURL(java.lang.String url) {
    return url;
  }

  public java.lang.String encodeUrl(java.lang.String url) {
    return url;
  }

  public java.lang.String encodeRedirectUrl(java.lang.String url) {
    return url;
  }

  public void sendError(int sc, java.lang.String msg) throws java.io.IOException {
    throw new RuntimeException("not implement");
  }

  public void sendError(int sc) throws java.io.IOException  {
    throw new RuntimeException("not implement");
  }

  public void sendRedirect(java.lang.String location) throws java.io.IOException {
    throw new RuntimeException("not implement");
  }

  public void setDateHeader(java.lang.String name, long date) {
    throw new RuntimeException("not implement");
  }

  public void addDateHeader(java.lang.String name, long date) {
    throw new RuntimeException("not implement");
  }

  public void setHeader(java.lang.String name, java.lang.String value) {
    headers.put(name, value);
  }

  public void addHeader(java.lang.String name, java.lang.String value) {
    headers.put(name, value);
  }

  public void setIntHeader(java.lang.String name, int value) {
    headers.put(name, Integer.toString(value));
  }

  public void addIntHeader(java.lang.String name, int value) {
    headers.put(name, Integer.toString(value));
  }

  public void setStatus(int sc) {
    this.status = sc;
  }

  public void setStatus(int sc, java.lang.String sm) {
    this.status = sc;
  }

  public int getStatus() {
    return status;
  }

  public java.lang.String getHeader(java.lang.String name) {
    return headers.get(name);
  }

  public java.util.Collection<java.lang.String> getHeaders(java.lang.String name) {
    ArrayList<String> l = new ArrayList<String>();
    String v = headers.get(name);
    if (v != null) {
      l.add(v);
    }
    return l;
  }

  public java.util.Collection<java.lang.String> getHeaderNames() {
    return headers.keySet();
  }

  // ServletInterface
  public java.lang.String getCharacterEncoding() {
    return "";
  }

  public java.lang.String getContentType() {
    return "";
  }

  public ServletOutputStream getOutputStream() throws java.io.IOException {
    return null;
  }

  public java.io.PrintWriter getWriter() throws java.io.IOException {
  }

  public void setCharacterEncoding(java.lang.String charset) {
    this.charset = charset;
  }


}
