package com.github.wovnio.wovnjava;

import static org.junit.Assert.assertArrayEquals;
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
import org.w3c.dom.NodeList;
import nu.validator.htmlparser.dom.*;

public class TranslateTest extends TestCase {

    public void testPunyCode() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, SAXException, IOException {
        String html = "<html><head>" +
            "<meta name=\"twitter:title\" content=\"ページタイトル\">" +
            "<meta name=\"twitter:url\" content=\"http://www.日本語.com/twitter\">" +
            "<meta name=\"twitter:image:src\" content=\"http://www.日本語.com/twitter.gif\">" +
            "<meta property=\"og:url\" content=\"http://www.日本語.com/og\">" +
            "<meta property=\"og:image\" content=\"http://www.日本語.com/og.gif\">" +
            makeHtml("script", "src") +
            makeHtml("link", "href") +
            makeHtml("a", "href") +
            makeHtml("img", "src") +
            makeHtml("input", "src") +
            makeHtml("iframe", "src") +
            "</body></html>";
        String[] expects = makeExpects();
        String[] expectScripts = new String[expects.length + 1];
        System.arraycopy(expects, 0, expectScripts, 1, expects.length);
        expectScripts[0] = "//j.wovn.io/1";
        String[] expectMetas = new String[] {
            "text/html; charset=UTF-8",
            "ページタイトル",
            "http://www.xn--wgv71a119e.com/twitter",
            "http://www.xn--wgv71a119e.com/twitter.gif",
            "http://www.xn--wgv71a119e.com/og",
            "http://www.xn--wgv71a119e.com/og.gif",
        };

        Document doc = parse(switchLang(html));

        assertArrayEquals(expectScripts, getAttrs(doc, "script", "src"));
        assertArrayEquals(expects, getAttrs(doc, "link", "href"));
        assertArrayEquals(expects, getAttrs(doc, "a", "href"));
        assertArrayEquals(expects, getAttrs(doc, "img", "src"));
        assertArrayEquals(expects, getAttrs(doc, "input", "src"));
        assertArrayEquals(expects, getAttrs(doc, "iframe", "src"));
        assertArrayEquals(expectMetas, getAttrs(doc, "meta", "content"));
    }

    private String makeHtml(String tag, String attr) {
        return makeTag(tag, attr, "http://example.com/") +
            makeTag(tag, attr, "http://example.com/path/to") +
            makeTag(tag, attr, "https://example.com/path/to") +
            makeTag(tag, attr, "//example.com/path/to") +
            makeTag(tag, attr, "//example.com/あいうえお") +
            makeTag(tag, attr, "http://www.日本語.com/") +
            makeTag(tag, attr, "http://www.日本語.com/path/to") +
            makeTag(tag, attr, "https://www.日本語.com/path/to") +
            makeTag(tag, attr, "//www.日本語.com/path/to") +
            makeTag(tag, attr, "//www.日本語.com/あいうえお");
    }

    private String[] makeExpects() {
        return new String[] {
            "http://example.com/",
            "http://example.com/path/to",
            "https://example.com/path/to",
            "//example.com/path/to",
            "//example.com/%E3%81%82%E3%81%84%E3%81%86%E3%81%88%E3%81%8A",
            "http://www.xn--wgv71a119e.com/",
            "http://www.xn--wgv71a119e.com/path/to",
            "https://www.xn--wgv71a119e.com/path/to",
            "//www.xn--wgv71a119e.com/path/to",
            "//www.xn--wgv71a119e.com/%E3%81%82%E3%81%84%E3%81%86%E3%81%88%E3%81%8A"
        };
    }

    private String makeTag(String tag, String attr, String value) {
        String closeTag = tag == "link" || tag == "img" ? "" : "</" + tag + ">";
        return "<" + tag + " " + attr + "=\"" + value + "\">" + closeTag;
    }

    private String[] getAttrs(Document doc, String tag, String attr) throws SAXException {
        NodeList nodes = doc.getElementsByTagName(tag);
        String[] attrs = new String[nodes.getLength()];
        for (int i = 0; i < nodes.getLength(); i++) {
            attrs[i] = nodes.item(i).getAttributes().getNamedItem(attr).getNodeValue();
        }
        return attrs;
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
        EasyMock.replay(mock);
        return mock;
    }
}
