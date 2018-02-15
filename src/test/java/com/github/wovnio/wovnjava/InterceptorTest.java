package com.github.wovnio.wovnjava;

import junit.framework.TestCase;

import org.easymock.EasyMock;

import java.util.HashMap;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;

public class InterceptorTest extends TestCase {

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

    private static FilterConfig mockConfigSubDomain() {
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

    public void testInterceptor() {
        FilterConfig mockConfig = mockConfigPath();
        Interceptor interceptor = new Interceptor(mockConfig);

        assertNotNull(interceptor);
    }

    public void testIsHtml() {
        assertEquals(false, Interceptor.isHtml(""));
        assertEquals(false, Interceptor.isHtml("xml"));
        assertEquals(false, Interceptor.isHtml("html"));
        assertEquals(false, Interceptor.isHtml("doctype"));
        assertEquals(false, Interceptor.isHtml("doctype html"));
        assertEquals(false, Interceptor.isHtml("<!-- -->"));

        assertEquals(true, Interceptor.isHtml("<?xml version=\"1.0\"?>"));
        assertEquals(false, Interceptor.isHtml("<?xmlversion=\"1.0\"?>"));
        assertEquals(true, Interceptor.isHtml("<?XML version=\"1.0\"?>"));
        assertEquals(true, Interceptor.isHtml("   <?xml version=\"1.0\"?>"));
        assertEquals(true, Interceptor.isHtml("\n<?xml version=\"1.0\"?>"));
        assertEquals(true, Interceptor.isHtml("\r\n<?xml version=\"1.0\"?>"));
        assertEquals(true, Interceptor.isHtml("\n\r<?xml version=\"1.0\"?>"));
        assertEquals(true, Interceptor.isHtml(" \n <?xml version=\"1.0\"?>"));
        assertEquals(true, Interceptor.isHtml(" \r\n <?xml version=\"1.0\"?>"));
        assertEquals(true, Interceptor.isHtml(" \n\r <?xml version=\"1.0\"?>"));
        assertEquals(false, Interceptor.isHtml("aaa\n<?xml version=\"1.0\"?>"));
        assertEquals(false, Interceptor.isHtml("aaa\r\n<?xml version=\"1.0\"?>"));
        assertEquals(false, Interceptor.isHtml("aaa\n\r<?xml version=\"1.0\"?>"));
        assertEquals(true, Interceptor.isHtml("<!-- --><?xml version=\"1.0\"?>"));
        assertEquals(true, Interceptor.isHtml("<!-- -->\n<?xml version=\"1.0\"?>"));
        assertEquals(true, Interceptor.isHtml("<!-- -->\r\n<?xml version=\"1.0\"?>"));
        assertEquals(true, Interceptor.isHtml("<!-- -->\n\r<?xml version=\"1.0\"?>"));

        assertEquals(true, Interceptor.isHtml("<html></html>"));
        assertEquals(true, Interceptor.isHtml("<html ></html>"));
        assertEquals(false, Interceptor.isHtml("<html1></html>"));
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
        assertEquals(true, Interceptor.isHtml("<!-- --><html></html>"));
        assertEquals(true, Interceptor.isHtml("<!-- -->\n<html></html>"));
        assertEquals(true, Interceptor.isHtml("<!-- -->\r\n<html></html>"));
        assertEquals(true, Interceptor.isHtml("<!-- -->\n\r<html></html>"));

        assertEquals(true, Interceptor.isHtml("<!DOCTYPE html><html></html>"));
        assertEquals(false, Interceptor.isHtml("<!DOCTYPEhtml><html></html>"));
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
        assertEquals(true, Interceptor.isHtml("<!-- --><!DOCTYPE html><html></html>"));
        assertEquals(true, Interceptor.isHtml("<!-- -->\n<!DOCTYPE html><html></html>"));
        assertEquals(true, Interceptor.isHtml("<!-- -->\r\n<!DOCTYPE html><html></html>"));
        assertEquals(true, Interceptor.isHtml("<!-- -->\n\r<!DOCTYPE html><html></html>"));
    }

    public void testDoctype() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String input = "<html></html>";
        String html = "<html lang=\"en\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><script async=\"true\" data-wovnio=\"key=2Wle3&amp;backend=true&amp;currentLang=en&amp;defaultLang=en&amp;urlPattern=path&amp;version=0.1.9\" src=\"//j.wovn.io/1\"> </script></head><body></body></html>";
        String xhtml = "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><script async=\"true\" data-wovnio=\"key=2Wle3&amp;backend=true&amp;currentLang=en&amp;defaultLang=en&amp;urlPattern=path&amp;version=0.1.9\" src=\"//j.wovn.io/1\"> </script></head><body></body></html>";

        assertDocType("<!DOCTYPE html>", input, html);
        assertDocType("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", input, html);
        assertDocType("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">", input, html);
        assertDocType("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Frameset//EN\" \"http://www.w3.org/TR/html4/frameset.dtd\">", input, html);
        assertDocType("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">", input, xhtml);
        assertDocType("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">", input, xhtml);
        assertDocType("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">", input, xhtml);
        assertDocType("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Frameset//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd\">", input, xhtml);
    }

    private void assertDocType(String doctype, String input, String expect) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        assertDocTypeWithCase(doctype, input, expect);
        assertDocTypeWithCase(doctype.replace(" ", "\n"), input, expect);
        assertDocTypeWithCase(doctype.replace(" ", "  "), input, expect);
        assertDocTypeWithCase(doctype.replace(" ", "\n  "), input, expect);
    }

    private void assertDocTypeWithCase(String doctype, String input, String expect) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        assertEquals(doctype + expect, switchLang(doctype + input));
        assertEquals(doctype.toUpperCase() + expect, switchLang(doctype.toUpperCase() + input));
        assertEquals(doctype.toLowerCase() + expect, switchLang(doctype.toLowerCase() + input));
    }

    private String switchLang(String html) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Interceptor interceptor = new Interceptor(mockConfigPath());
        Method method = interceptor.getClass().getDeclaredMethod("switchLang", String.class, Values.class, HashMap.class, String.class, Headers.class);
        method.setAccessible(true);
        Values values = new Values("");
        HashMap<String, String> url = new HashMap<String, String>();
        String lang = "en";
        HttpServletRequest mockRequest = mockRequestPath();
        Headers headers = new Headers(mockRequest, new Settings(mockConfigPath()));
        return (String)method.invoke(interceptor, html, values, url, lang, headers);
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
}
