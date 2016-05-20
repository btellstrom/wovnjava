package com.github.wovnio.wovnjava;

import junit.framework.TestCase;

import org.easymock.EasyMock;

import javax.servlet.FilterConfig;

public class InterceptorTest extends TestCase {

    private static FilterConfig mockConfigPath() {
        FilterConfig mock = EasyMock.createMock(FilterConfig.class);
        EasyMock.expect(mock.getInitParameter("userToken")).andReturn("2Wle3");
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
        EasyMock.replay(mock);
        return mock;
    }

    private static FilterConfig mockConfigSubDomain() {
        FilterConfig mock = EasyMock.createMock(FilterConfig.class);
        EasyMock.expect(mock.getInitParameter("userToken")).andReturn("2Wle3");
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
        EasyMock.replay(mock);
        return mock;
    }

    private static FilterConfig mockConfigQuery() {
        FilterConfig mock = EasyMock.createMock(FilterConfig.class);
        EasyMock.expect(mock.getInitParameter("userToken")).andReturn("2Wle3");
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
        EasyMock.replay(mock);
        return mock;
    }

    public void testInterceptor() {
        FilterConfig mockConfig = mockConfigPath();
        Interceptor interceptor = new Interceptor(mockConfig);

        assertNotNull(interceptor);
    }

    public void testIsHtml() {
        assertEquals(false, Interceptor.isHtml(""));
        assertEquals(false, Interceptor.isHtml("html"));
        assertEquals(false, Interceptor.isHtml("doctype"));
        assertEquals(false, Interceptor.isHtml("doctype html"));

        assertEquals(true, Interceptor.isHtml("<html></html>"));
        assertEquals(false, Interceptor.isHtml("aaa<html></html>"));
        assertEquals(true, Interceptor.isHtml("<HTML></HTML>"));
        assertEquals(true, Interceptor.isHtml(" <html></html>"));
        assertEquals(true, Interceptor.isHtml("\n<html></html>"));
        assertEquals(true, Interceptor.isHtml("\r\n<html></html>"));
        assertEquals(true, Interceptor.isHtml("\n\r<html></html>"));
        assertEquals(true, Interceptor.isHtml("  \n  <html></html>"));
        assertEquals(true, Interceptor.isHtml("  \r\n  <html></html>"));
        assertEquals(true, Interceptor.isHtml("  \n\r  <html></html>"));
        assertEquals(false, Interceptor.isHtml("aaa\n<html></html>"));
        assertEquals(false, Interceptor.isHtml("aaa\r\n<html></html>"));
        assertEquals(false, Interceptor.isHtml("aaa\n\r<html></html>"));

        assertEquals(true, Interceptor.isHtml("<!DOCTYPE html><html></html>"));
        assertEquals(true, Interceptor.isHtml("<!DOCTYPE   html><html></html>"));
        assertEquals(false, Interceptor.isHtml("aaa<!DOCTYPE html><html></html>"));
        assertEquals(true, Interceptor.isHtml("<!doctype html><html></html>"));
        assertEquals(true, Interceptor.isHtml("  <!DOCTYPE html><html></html>"));
        assertEquals(true, Interceptor.isHtml("\n<!DOCTYPE html><html></html>"));
        assertEquals(true, Interceptor.isHtml("\r\n<!DOCTYPE html><html></html>"));
        assertEquals(true, Interceptor.isHtml("\n\r<!DOCTYPE html><html></html>"));
        assertEquals(true, Interceptor.isHtml("  \n  <!DOCTYPE html><html></html>"));
        assertEquals(true, Interceptor.isHtml("  \r\n  <!DOCTYPE html><html></html>"));
        assertEquals(true, Interceptor.isHtml("  \n\r  <!DOCTYPE html><html></html>"));
        assertEquals(false, Interceptor.isHtml("aaa\n<!DOCTYPE html><html></html>"));
        assertEquals(false, Interceptor.isHtml("aaa\r\n<!DOCTYPE html><html></html>"));
        assertEquals(false, Interceptor.isHtml("aaa\n\r<!DOCTYPE html><html></html>"));
    }

}