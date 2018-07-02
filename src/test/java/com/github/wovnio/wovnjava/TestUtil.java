package com.github.wovnio.wovnjava;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.FilterConfig;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;
import org.easymock.EasyMock;


public class TestUtil {
    private static final HashMap<String, String> emptySettings = new HashMap<String, String>();

    public static Settings makeSettings() {
        return makeSettings(emptySettings);
    }

    public static Settings makeSettings(HashMap<String, String> option) {
        FilterConfig mock = EasyMock.createMock(FilterConfig.class);
        String[] keys = {"userToken", "projectToken", "sitePrefixPath", "secretKey", "urlPattern", "urlPatternReg", "query", "apiUrl", "defaultLang", "supportedLangs", "testMode", "testUrl", "useProxy", "debugMode", "originalUrlHeader", "originalQueryStringHeader", "strictHtmlCheck", "deleteInvalidClosingTag", "deleteInvalidUTF8", "connectTimeout", "readTimeout"};
        for (int i=0; i<keys.length; ++i) {
            String key = keys[i];
            String val = option.get(key);
            val = val == null ? "" : val;
            EasyMock.expect(mock.getInitParameter(key)).andReturn(val);
        }
        EasyMock.replay(mock);
        return new Settings(mock);
    }

    public static HttpServletRequest mockRequestPath(String path) {
        return mockRequestPath(path, null, new RequestDispatcherMock());
    }

    public static HttpServletRequest mockRequestPath(String path, String replacedPath, RequestDispatcherMock dispatcher) {
        HttpServletRequest mock = EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(mock.getScheme()).andReturn("https");
        EasyMock.expect(mock.getRemoteHost()).andReturn("example.com");
        EasyMock.expect(mock.getRequestURI()).andReturn(path).atLeastOnce();
        EasyMock.expect(mock.getServerName()).andReturn("example.com").atLeastOnce();
        EasyMock.expect(mock.getQueryString()).andReturn("").atLeastOnce();
        EasyMock.expect(mock.getServerPort()).andReturn(443).atLeastOnce();
        EasyMock.expect(mock.getHeader("Location")).andReturn(path).atLeastOnce();
        EasyMock.expect(mock.getRequestDispatcher(replacedPath == null ? EasyMock.anyString() : replacedPath)).andReturn(dispatcher);
        EasyMock.replay(mock);
        return mock;
    }

    public static ServletResponse mockResponse(String contentType, String encoding) throws IOException {
        HttpServletResponse mock = EasyMock.createMock(HttpServletResponse.class);
        mock.setContentLength(EasyMock.anyInt());
        EasyMock.expectLastCall();
        mock.setCharacterEncoding("utf-8");
        EasyMock.expectLastCall();
        EasyMock.expect(mock.getWriter()).andReturn(new PrintWriter(new StringWriter()));
        EasyMock.expect(mock.getContentType()).andReturn(contentType).atLeastOnce();
        EasyMock.expect(mock.getCharacterEncoding()).andReturn(encoding);
        EasyMock.replay(mock);
        return mock;
    }

    public static FilterConfig mockFilterConfig() {
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
        EasyMock.expect(mock.getInitParameter("deleteInvalidClosingTag")).andReturn("");
        EasyMock.expect(mock.getInitParameter("deleteInvalidUTF8")).andReturn("");
        EasyMock.expect(mock.getInitParameter("connectTimeout")).andReturn("");
        EasyMock.expect(mock.getInitParameter("readTimeout")).andReturn("");
        EasyMock.replay(mock);
        return mock;
    }

    public static FilterChainMock doServletFilter(String contentType, String path) throws ServletException, IOException {
        return doServletFilter(contentType, path, path);
    }

    public static FilterChainMock doServletFilter(String contentType, String path, String forwardPath) throws ServletException, IOException {
        RequestDispatcherMock dispatcher = new RequestDispatcherMock();
        HttpServletRequest req = mockRequestPath(path, forwardPath, dispatcher);
        ServletResponse res = TestUtil.mockResponse(contentType, "");
        FilterConfig filterConfig = mockFilterConfig();
        FilterChainMock filterChain = new FilterChainMock();
        WovnServletFilter filter = new WovnServletFilter();
        filter.init(filterConfig);
        filter.doFilter(req, res, filterChain);
        filterChain.req = filterChain.req == null ? dispatcher.req : filterChain.req;
        filterChain.res = filterChain.res == null ? dispatcher.res : filterChain.res;
        return filterChain;
    }
}
