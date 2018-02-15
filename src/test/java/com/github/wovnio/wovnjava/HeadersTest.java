package com.github.wovnio.wovnjava;

import junit.framework.TestCase;

import org.easymock.EasyMock;

import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;

public class HeadersTest extends TestCase {

    private static FilterConfig mockConfigPath() {
        FilterConfig mock = EasyMock.createMock(FilterConfig.class);
        EasyMock.expect(mock.getInitParameter("userToken")).andReturn("2Wle3");
        EasyMock.expect(mock.getInitParameter("projectToken")).andReturn("2Wle3");
        EasyMock.expect(mock.getInitParameter("sitePrefixPath")).andReturn("");
        EasyMock.expect(mock.getInitParameter("secretKey")).andReturn("secret");
        EasyMock.expect(mock.getInitParameter("urlPattern")).andReturn("");
        EasyMock.expect(mock.getInitParameter("urlPatternReg")).andReturn("");
        EasyMock.expect(mock.getInitParameter("query")).andReturn("");
        EasyMock.expect(mock.getInitParameter("apiUrl")).andReturn("");
        EasyMock.expect(mock.getInitParameter("defaultLang")).andReturn("");
        EasyMock.expect(mock.getInitParameter("supportedLangs")).andReturn("");
        EasyMock.expect(mock.getInitParameter("testMode")).andReturn("");
        EasyMock.expect(mock.getInitParameter("testUrl")).andReturn("");
        EasyMock.expect(mock.getInitParameter("useProxy")).andReturn("");
        EasyMock.expect(mock.getInitParameter("debugMode")).andReturn("");
        EasyMock.expect(mock.getInitParameter("originalUrlHeader")).andReturn("");
        EasyMock.expect(mock.getInitParameter("originalQueryStringHeader")).andReturn("");
        EasyMock.expect(mock.getInitParameter("strictHtmlCheck")).andReturn("");
        EasyMock.replay(mock);

        return mock;
    }
    private static FilterConfig mockConfigSubdomain() {
        FilterConfig mock = EasyMock.createMock(FilterConfig.class);
        EasyMock.expect(mock.getInitParameter("userToken")).andReturn("2Wle3");
        EasyMock.expect(mock.getInitParameter("projectToken")).andReturn("2Wle3");
        EasyMock.expect(mock.getInitParameter("sitePrefixPath")).andReturn("");
        EasyMock.expect(mock.getInitParameter("secretKey")).andReturn("secret");
        EasyMock.expect(mock.getInitParameter("urlPattern")).andReturn("subdomain");
        EasyMock.expect(mock.getInitParameter("urlPatternReg")).andReturn("");
        EasyMock.expect(mock.getInitParameter("query")).andReturn("");
        EasyMock.expect(mock.getInitParameter("apiUrl")).andReturn("");
        EasyMock.expect(mock.getInitParameter("defaultLang")).andReturn("");
        EasyMock.expect(mock.getInitParameter("supportedLangs")).andReturn("");
        EasyMock.expect(mock.getInitParameter("testMode")).andReturn("");
        EasyMock.expect(mock.getInitParameter("testUrl")).andReturn("");
        EasyMock.expect(mock.getInitParameter("useProxy")).andReturn("");
        EasyMock.expect(mock.getInitParameter("debugMode")).andReturn("");
        EasyMock.expect(mock.getInitParameter("originalUrlHeader")).andReturn("");
        EasyMock.expect(mock.getInitParameter("originalQueryStringHeader")).andReturn("");
        EasyMock.expect(mock.getInitParameter("strictHtmlCheck")).andReturn("");
        EasyMock.replay(mock);

        return mock;
    }
    private static FilterConfig mockConfigQuery() {
        FilterConfig mock = EasyMock.createMock(FilterConfig.class);
        EasyMock.expect(mock.getInitParameter("userToken")).andReturn("2Wle3");
        EasyMock.expect(mock.getInitParameter("projectToken")).andReturn("2Wle3");
        EasyMock.expect(mock.getInitParameter("sitePrefixPath")).andReturn("");
        EasyMock.expect(mock.getInitParameter("secretKey")).andReturn("secret");
        EasyMock.expect(mock.getInitParameter("urlPattern")).andReturn("query");
        EasyMock.expect(mock.getInitParameter("urlPatternReg")).andReturn("");
        EasyMock.expect(mock.getInitParameter("query")).andReturn("");
        EasyMock.expect(mock.getInitParameter("apiUrl")).andReturn("");
        EasyMock.expect(mock.getInitParameter("defaultLang")).andReturn("");
        EasyMock.expect(mock.getInitParameter("supportedLangs")).andReturn("");
        EasyMock.expect(mock.getInitParameter("testMode")).andReturn("");
        EasyMock.expect(mock.getInitParameter("testUrl")).andReturn("");
        EasyMock.expect(mock.getInitParameter("useProxy")).andReturn("");
        EasyMock.expect(mock.getInitParameter("debugMode")).andReturn("");
        EasyMock.expect(mock.getInitParameter("originalUrlHeader")).andReturn("");
        EasyMock.expect(mock.getInitParameter("originalQueryStringHeader")).andReturn("");
        EasyMock.expect(mock.getInitParameter("strictHtmlCheck")).andReturn("");
        EasyMock.replay(mock);

        return mock;
    }

    private static FilterConfig mockConfigQueryParameter() {
        FilterConfig mock = EasyMock.createMock(FilterConfig.class);
        EasyMock.expect(mock.getInitParameter("userToken")).andReturn("2Wle3");
        EasyMock.expect(mock.getInitParameter("projectToken")).andReturn("2Wle3");
        EasyMock.expect(mock.getInitParameter("sitePrefixPath")).andReturn("");
        EasyMock.expect(mock.getInitParameter("secretKey")).andReturn("secret");
        EasyMock.expect(mock.getInitParameter("urlPattern")).andReturn("query");
        EasyMock.expect(mock.getInitParameter("urlPatternReg")).andReturn("");
        EasyMock.expect(mock.getInitParameter("query")).andReturn("abc");
        EasyMock.expect(mock.getInitParameter("apiUrl")).andReturn("");
        EasyMock.expect(mock.getInitParameter("defaultLang")).andReturn("");
        EasyMock.expect(mock.getInitParameter("supportedLangs")).andReturn("");
        EasyMock.expect(mock.getInitParameter("testMode")).andReturn("");
        EasyMock.expect(mock.getInitParameter("testUrl")).andReturn("");
        EasyMock.expect(mock.getInitParameter("useProxy")).andReturn("");
        EasyMock.expect(mock.getInitParameter("debugMode")).andReturn("");
        EasyMock.expect(mock.getInitParameter("originalUrlHeader")).andReturn("");
        EasyMock.expect(mock.getInitParameter("originalQueryStringHeader")).andReturn("");
        EasyMock.expect(mock.getInitParameter("strictHtmlCheck")).andReturn("");
        EasyMock.replay(mock);

        return mock;
    }

    private static FilterConfig mockConfigQueryParameterAAA() {
        FilterConfig mock = EasyMock.createMock(FilterConfig.class);
        EasyMock.expect(mock.getInitParameter("userToken")).andReturn("2Wle3");
        EasyMock.expect(mock.getInitParameter("projectToken")).andReturn("2Wle3");
        EasyMock.expect(mock.getInitParameter("sitePrefixPath")).andReturn("");
        EasyMock.expect(mock.getInitParameter("secretKey")).andReturn("secret");
        EasyMock.expect(mock.getInitParameter("urlPattern")).andReturn("query");
        EasyMock.expect(mock.getInitParameter("urlPatternReg")).andReturn("");
        EasyMock.expect(mock.getInitParameter("query")).andReturn("AAA");
        EasyMock.expect(mock.getInitParameter("apiUrl")).andReturn("");
        EasyMock.expect(mock.getInitParameter("defaultLang")).andReturn("");
        EasyMock.expect(mock.getInitParameter("supportedLangs")).andReturn("");
        EasyMock.expect(mock.getInitParameter("testMode")).andReturn("");
        EasyMock.expect(mock.getInitParameter("testUrl")).andReturn("");
        EasyMock.expect(mock.getInitParameter("useProxy")).andReturn("");
        EasyMock.expect(mock.getInitParameter("debugMode")).andReturn("");
        EasyMock.expect(mock.getInitParameter("originalUrlHeader")).andReturn("");
        EasyMock.expect(mock.getInitParameter("originalQueryStringHeader")).andReturn("");
        EasyMock.expect(mock.getInitParameter("strictHtmlCheck")).andReturn("");
        EasyMock.replay(mock);

        return mock;
    }

    private static FilterConfig mockConfigOriginalHeaders() {
        FilterConfig mock = EasyMock.createMock(FilterConfig.class);
        EasyMock.expect(mock.getInitParameter("userToken")).andReturn("2Wle3");
        EasyMock.expect(mock.getInitParameter("projectToken")).andReturn("2Wle3");
        EasyMock.expect(mock.getInitParameter("sitePrefixPath")).andReturn("");
        EasyMock.expect(mock.getInitParameter("secretKey")).andReturn("secret");
        EasyMock.expect(mock.getInitParameter("urlPattern")).andReturn("");
        EasyMock.expect(mock.getInitParameter("urlPatternReg")).andReturn("");
        EasyMock.expect(mock.getInitParameter("query")).andReturn("baz");
        EasyMock.expect(mock.getInitParameter("apiUrl")).andReturn("");
        EasyMock.expect(mock.getInitParameter("defaultLang")).andReturn("");
        EasyMock.expect(mock.getInitParameter("supportedLangs")).andReturn("");
        EasyMock.expect(mock.getInitParameter("testMode")).andReturn("");
        EasyMock.expect(mock.getInitParameter("testUrl")).andReturn("");
        EasyMock.expect(mock.getInitParameter("useProxy")).andReturn("");
        EasyMock.expect(mock.getInitParameter("debugMode")).andReturn("");
        EasyMock.expect(mock.getInitParameter("originalUrlHeader")).andReturn("REDIRECT_URL");
        EasyMock.expect(mock.getInitParameter("originalQueryStringHeader")).andReturn("REDIRECT_QUERY_STRING");
        EasyMock.expect(mock.getInitParameter("strictHtmlCheck")).andReturn("");
        EasyMock.replay(mock);

        return mock;
    }

    private static HttpServletRequest mockRequestPath() {
        HttpServletRequest mock = EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(mock.getScheme()).andReturn("https");
        EasyMock.expect(mock.getRemoteHost()).andReturn("example.com");
        EasyMock.expect(mock.getRequestURI()).andReturn("/ja/test").atLeastOnce();
        EasyMock.expect(mock.getServerName()).andReturn("example.com").atLeastOnce();
        EasyMock.expect(mock.getQueryString()).andReturn("").atLeastOnce();
        EasyMock.expect(mock.getServerPort()).andReturn(443).atLeastOnce();
        EasyMock.replay(mock);

        return mock;
    }
    private static HttpServletRequest mockRequestSubdomain() {
        HttpServletRequest mock = EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(mock.getScheme()).andReturn("https");
        EasyMock.expect(mock.getRemoteHost()).andReturn("ja.example.com");
        EasyMock.expect(mock.getRequestURI()).andReturn("/test").atLeastOnce();
        EasyMock.expect(mock.getServerName()).andReturn("ja.example.com").atLeastOnce();
        EasyMock.expect(mock.getQueryString()).andReturn("").atLeastOnce();
        EasyMock.expect(mock.getServerPort()).andReturn(443).atLeastOnce();
        EasyMock.replay(mock);

        return mock;
    }
    private static HttpServletRequest mockRequestQuery() {
        HttpServletRequest mock = EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(mock.getScheme()).andReturn("https");
        EasyMock.expect(mock.getRemoteHost()).andReturn("example.com");
        EasyMock.expect(mock.getRequestURI()).andReturn("/test").atLeastOnce();
        EasyMock.expect(mock.getServerName()).andReturn("example.com").atLeastOnce();
        EasyMock.expect(mock.getQueryString()).andReturn("wovn=ja").atLeastOnce();
        EasyMock.expect(mock.getServerPort()).andReturn(443).atLeastOnce();
        EasyMock.replay(mock);

        return mock;
    }

    private static HttpServletRequest mockRequestQueryParameter() {
        HttpServletRequest mock = EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(mock.getScheme()).andReturn("https");
        EasyMock.expect(mock.getRemoteHost()).andReturn("example.com");
        EasyMock.expect(mock.getRequestURI()).andReturn("/test").atLeastOnce();
        EasyMock.expect(mock.getServerName()).andReturn("example.com").atLeastOnce();
        EasyMock.expect(mock.getQueryString()).andReturn("def=456&wovn=ja&abc=123").atLeastOnce();
        EasyMock.expect(mock.getServerPort()).andReturn(443).atLeastOnce();
        EasyMock.replay(mock);

        return mock;
    }

    private static HttpServletRequest mockRequestServerPort() {
        HttpServletRequest mock = EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(mock.getScheme()).andReturn("https");
        EasyMock.expect(mock.getRemoteHost()).andReturn("example.com");
        EasyMock.expect(mock.getRequestURI()).andReturn("/ja/test").atLeastOnce();
        EasyMock.expect(mock.getServerName()).andReturn("example.com").atLeastOnce();
        EasyMock.expect(mock.getQueryString()).andReturn("").atLeastOnce();
        EasyMock.expect(mock.getServerPort()).andReturn(8080).atLeastOnce();
        EasyMock.replay(mock);

        return mock;
    }

    private static HttpServletRequest mockRequestOriginalHeaders() {
        HttpServletRequest mock = EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(mock.getScheme()).andReturn("https");
        EasyMock.expect(mock.getRemoteHost()).andReturn("example.com");
        EasyMock.expect(mock.getRequestURI()).andReturn("/ja/test").atLeastOnce();
        EasyMock.expect(mock.getServerName()).andReturn("example.com").atLeastOnce();
        EasyMock.expect(mock.getQueryString()).andReturn("").atLeastOnce();
        EasyMock.expect(mock.getServerPort()).andReturn(443).atLeastOnce();
        EasyMock.expect(mock.getHeader("REDIRECT_URL")).andReturn("/foo/bar").atLeastOnce();
        EasyMock.expect(mock.getHeader("REDIRECT_QUERY_STRING")).andReturn("baz=123").atLeastOnce();
        EasyMock.replay(mock);

        return mock;

    }

    public void testHeaders() {
        HttpServletRequest mockRequest = mockRequestPath();
        FilterConfig mockConfig = mockConfigPath();

        Settings s = new Settings(mockConfig);
        Headers h = new Headers(mockRequest, s);

        assertNotNull(h);
    }

    public void testHeadersWithoutQueryParameter() {
        HttpServletRequest mockRequest = mockRequestQueryParameter();
        FilterConfig mockConfig = mockConfigQuery();

        Settings s = new Settings(mockConfig);
        Headers h = new Headers(mockRequest, s);

        assertNotNull(h);
        assertEquals("", h.query);
    }

    public void testHeadersWithQueryParameter() {
        HttpServletRequest mockRequest = mockRequestQueryParameter();
        FilterConfig mockConfig = mockConfigQueryParameter();

        Settings s = new Settings(mockConfig);
        Headers h = new Headers(mockRequest, s);

        assertNotNull(h);
        assertEquals("?abc=123", h.query);
    }

    public void testGetPathLangPath() {
        HttpServletRequest mockRequest = mockRequestPath();
        FilterConfig mockConfig = mockConfigPath();

        Settings s = new Settings(mockConfig);
        Headers h = new Headers(mockRequest, s);

        assertEquals("ja", h.getPathLang());
    }

    public void testGetPathLangSubdomain() {
        HttpServletRequest mockRequest = mockRequestSubdomain();
        FilterConfig mockConfig = mockConfigSubdomain();

        Settings s = new Settings(mockConfig);
        Headers h = new Headers(mockRequest, s);

        assertEquals("ja", h.getPathLang());
    }

    public void testGetPathLangQuery() {
        HttpServletRequest mockRequest = mockRequestQuery();
        FilterConfig mockConfig = mockConfigQuery();

        Settings s = new Settings(mockConfig);
        Headers h = new Headers(mockRequest, s);

        assertEquals("ja", h.getPathLang());
    }

    public void testRedirectLocationPath() {
        HttpServletRequest mockRequest = mockRequestPath();
        FilterConfig mockConfig = mockConfigPath();

        Settings s = new Settings(mockConfig);
        Headers h = new Headers(mockRequest, s);

//        assertEquals("", h.redirectLocation("ja"));
    }
    public void testRedirectLocationSubdomain() {

    }
    public void testRedirectLocationQuery() {

    }

    public void testRemoveLangPath() {
        HttpServletRequest mockRequest = mockRequestPath();
        FilterConfig mockConfig = mockConfigPath();

        Settings s = new Settings(mockConfig);
        Headers h = new Headers(mockRequest, s);

        assertEquals("example.com/test", h.removeLang("example.com/ja/test", null));
    }
    public void testRemoveLangSubdomain() {
        HttpServletRequest mockRequest = mockRequestSubdomain();
        FilterConfig mockConfig = mockConfigSubdomain();

        Settings s = new Settings(mockConfig);
        Headers h = new Headers(mockRequest, s);

        assertEquals("example.com/test", h.removeLang("ja.example.com/test", null));
    }
    public void testRemoveLangQuery() {
        HttpServletRequest mockRequest = mockRequestQuery();
        FilterConfig mockConfig = mockConfigQuery();

        Settings s = new Settings(mockConfig);
        Headers h = new Headers(mockRequest, s);

        assertEquals("example.com/test", h.removeLang("example.com/test?wovn=ja", null));
    }

    public void testNotMatchQuery() {
        HttpServletRequest mockRequest = mockRequestQueryParameter();
        FilterConfig mockConfig = mockConfigQueryParameterAAA();

        Settings s = new Settings(mockConfig);
        Headers h = new Headers(mockRequest, s);

        assertEquals("", h.query);
    }

    public void testServerPort() {
        HttpServletRequest mockRequest = mockRequestServerPort();
        FilterConfig mockConfig = mockConfigPath();

        Settings s = new Settings(mockConfig);
        Headers h = new Headers(mockRequest, s);

        assertEquals("example.com:8080/test", h.pageUrl);
    }

    public void testOriginalHeaders() {
        HttpServletRequest mockRequest = mockRequestOriginalHeaders();
        FilterConfig mockConfig = mockConfigOriginalHeaders();

        Settings s = new Settings(mockConfig);
        Headers h = new Headers(mockRequest, s);

        assertEquals("/foo/bar", h.pathName);
        assertEquals("?baz=123", h.query);
        assertEquals("example.com/foo/bar?baz=123", h.pageUrl);
    }
}
