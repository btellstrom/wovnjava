package com.github.wovnio.wovnjava;

import junit.framework.TestCase;

import org.easymock.EasyMock;

import java.io.StringReader;
import java.io.IOException;
import java.util.HashMap;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import nu.validator.htmlparser.dom.*;

public class TranslateTest extends TestCase {

    public void testPunyCode() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, SAXException, IOException {
        String html = "<html><head>" +
            "<script src=\"//j.wovn.io/1\"></script>" +
            "<link href=\"http://www.日本語ドメイン.co.jp/にほんご\" rel=\"canonical\">" +
            "</head><body>" +
            "<a href=\"https://www.français.fr/path/to\">link</a>" +
            "<img src=\"//www.日本語ドメイン.co.jp/a.png\">" +
            "<iframe src=\"//www.日本語ドメイン.co.jp/a.html\">" +
            "</body></html>";
        Document doc = parse(switchLang(html));
        assertEquals("//j.wovn.io/1", get(doc, "script", "src")); // does not change
        assertEquals("//www.xn--eckwd4c7c5976acvb2w6i.co.jp/a.png", get(doc, "img", "src"));
        assertEquals("http://www.xn--eckwd4c7c5976acvb2w6i.co.jp/%E3%81%AB%E3%81%BB%E3%82%93%E3%81%94", get(doc, "link", "href"));
        assertEquals("https://www.xn--franais-xxa.fr/path/to", get(doc, "a", "href"));
        assertEquals("//www.xn--eckwd4c7c5976acvb2w6i.co.jp/a.html", get(doc, "iframe", "src"));
    }

    private String get(Document doc, String tag, String attr) throws SAXException {
        return doc.getElementsByTagName(tag).item(0).getAttributes().getNamedItem(attr).getNodeValue();
    }

    private Document parse(String html) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, SAXException, IOException {
        HtmlDocumentBuilder builder = new HtmlDocumentBuilder();
        StringReader reader = new StringReader(html);
        return builder.parse(new InputSource(reader));
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

    private static FilterConfig mockConfigPath() {
        FilterConfig mock = EasyMock.createMock(FilterConfig.class);
        EasyMock.expect(mock.getInitParameter("userToken")).andReturn("2Wle3");
        EasyMock.expect(mock.getInitParameter("projectToken")).andReturn("2Wle3");
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
}
