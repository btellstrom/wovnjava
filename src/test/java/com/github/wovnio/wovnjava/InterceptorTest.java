package com.github.wovnio.wovnjava;

import junit.framework.TestCase;

import org.easymock.EasyMock;

import java.util.HashMap;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;

public class InterceptorTest extends TestCase {

    private static FilterConfig mockSpecificConfig(HashMap<String, String> option) {
        FilterConfig mock = EasyMock.createMock(FilterConfig.class);
        String[] keys = {"userToken", "projectToken", "sitePrefixPath", "secretKey", "urlPattern", "urlPatternReg", "query", "apiUrl", "defaultLang", "supportedLangs", "testMode", "testUrl", "useProxy", "debugMode", "originalUrlHeader", "originalQueryStringHeader", "strictHtmlCheck", "deleteInvalidClosingTag", "deleteInvalidUTF8"};
        for (int i=0; i<keys.length; ++i) {
            String key = keys[i];
            String val = option.get(key);
            val = val == null ? "" : val;
            EasyMock.expect(mock.getInitParameter(key)).andReturn(val);
        }
        EasyMock.replay(mock);
        return mock;
    }

    private static FilterConfig mockConfigPath() {
        HashMap<String, String> option = new HashMap<String, String>(){ {
            put("userToken", "2Wle3");
            put("projectToken", "2Wle3");
            put("secretKey", "secret");
        } };
        return mockSpecificConfig(option);
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
        String html = "<html lang=\"en\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><script async=\"true\" data-wovnio=\"key=2Wle3&amp;backend=true&amp;currentLang=en&amp;defaultLang=en&amp;urlPattern=path&amp;version=0.2.1\" src=\"//j.wovn.io/1\"> </script></head><body></body></html>";
        String xhtml = "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><script async=\"true\" data-wovnio=\"key=2Wle3&amp;backend=true&amp;currentLang=en&amp;defaultLang=en&amp;urlPattern=path&amp;version=0.2.1\" src=\"//j.wovn.io/1\"> </script></head><body></body></html>";

        assertDocType("<!DOCTYPE html>", input, html);
        assertDocType("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", input, html);
        assertDocType("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">", input, html);
        assertDocType("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Frameset//EN\" \"http://www.w3.org/TR/html4/frameset.dtd\">", input, html);
        assertDocType("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">", input, xhtml);
        assertDocType("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">", input, xhtml);
        assertDocType("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">", input, xhtml);
        assertDocType("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Frameset//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd\">", input, xhtml);
    }

    public void testSitePrefixPath() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        assertEquals("/global/fr/file", getTranslatedLink("/global/", "/global/", "/global/file", "fr"));
        assertEquals("/global/fr/dir/", getTranslatedLink("/global/", "/global/", "/global/dir/", "fr"));
        assertEquals("/global/fr/file", getTranslatedLink("/global/", "/global/", "file", "fr"));
        assertEquals("/global/fr/dir/", getTranslatedLink("/global/", "/global/", "dir/", "fr"));
        assertEquals("/global/fr/dir/../file", getTranslatedLink("/global/", "/global/dir/", "../file", "fr"));
        assertEquals("/global/fr/dir/../dir/", getTranslatedLink("/global/", "/global/dir/", "../dir/", "fr"));
        assertEquals("/global/fr/dir/../file", getTranslatedLink("/global/", "/global/dir/file", "../file", "fr"));
        assertEquals("/global/fr/dir/../dir/", getTranslatedLink("/global/", "/global/dir/file", "../dir/", "fr"));
    }

    public void testNoSitePrefix() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        assertEquals("/fr/global/file", getTranslatedLink("", "/global/", "/global/file", "fr"));
        assertEquals("/fr/global/dir/", getTranslatedLink("", "/global/", "/global/dir/", "fr"));
        assertEquals("/fr/global/file", getTranslatedLink("", "/global/", "file", "fr"));
        assertEquals("/fr/global/dir/", getTranslatedLink("", "/global/", "dir/", "fr"));
        assertEquals("/fr/global/dir/../file", getTranslatedLink("", "/global/dir/", "../file", "fr"));
        assertEquals("/fr/global/dir/../dir/", getTranslatedLink("", "/global/dir/", "../dir/", "fr"));
        assertEquals("/fr/global/dir/../file", getTranslatedLink("", "/global/dir/file", "../file", "fr"));
        assertEquals("/fr/global/dir/../dir/", getTranslatedLink("", "/global/dir/file", "../dir/", "fr"));
    }

    public void testNoDeleteInvalidUTF8() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String result = switchLang(new String(invalidUtf8));
        assertEquals("1Az", between("<title>", "</title>", result));
        assertEquals("2�z", between("<h1>", "</h1>", result));
        assertEquals("3&#130;z", between("<h2>", "</h2>", result));
    }

    public void testDeleteInvalidUTF8() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String result = deleteInvalidUTF8(new String(invalidUtf8));
        assertEquals("1Az", between("<title>", "</title>", result));
        assertEquals("2z", between("<h1>", "</h1>", result));
        assertEquals("3z", between("<h2>", "</h2>", result));
    }

    private String between(String prefix, String suffix, String target) {
        int begin = target.indexOf(prefix);
        int end = target.indexOf(suffix);
        if (begin >= 0 && end >= 0) {
            return target.substring(begin + prefix.length(), end);
        } else {
            return "";
        }
    }

    private String getTranslatedLink(final String sitePrefixPath, String requestPath, String linkPath, String lang) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String html = "<a href=" + linkPath + ">link</a>";
        HashMap<String, String> option = new HashMap<String, String>(){ {
            put("sitePrefixPath", sitePrefixPath);
            put("supportedLangs", "ar,bg,zh-CHS,zh-CHT,da,nl,en,fi,fr,de,el,he,id,it,ja,ko,ms,my,ne,no,pl,pt,ru,es,sv,th,hi,tr,uk,vi");
        } };
        FilterConfig config = mockSpecificConfig(option);
        HttpServletRequest request = mockRequestPath(requestPath);
        String result = switchLang(html, config, request, lang);
        return result.substring(result.indexOf("href=") + 6, result.indexOf(">link<") - 1);
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
        return switchLang(html, mockConfigPath(), mockRequestPath("/ja/test"), "en");
    }

    private String switchLang(String html, FilterConfig config, HttpServletRequest mockRequest, String lang) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Interceptor interceptor = new Interceptor(config);
        Method method = interceptor.getClass().getDeclaredMethod("switchLang", String.class, Values.class, HashMap.class, String.class, Headers.class);
        method.setAccessible(true);
        Values values = new Values("");
        HashMap<String, String> url = new HashMap<String, String>();
        Headers headers = new Headers(mockRequest, new Settings(mockConfigPath()));
        return (String)method.invoke(interceptor, html, values, url, lang, headers);
    }

    private String deleteInvalidUTF8(String html) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        HashMap<String, String> option = new HashMap<String, String>() {{
            put("deleteInvalidUTF8", "1");
        }};
        FilterConfig config = mockSpecificConfig(option);
        return switchLang(html, config, mockRequestPath("/ja/test"), "en");
    }

    private static HttpServletRequest mockRequestPath(String path) {
        HttpServletRequest mock = EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(mock.getScheme()).andReturn("https");
        EasyMock.expect(mock.getRemoteHost()).andReturn("example.com");
        EasyMock.expect(mock.getRequestURI()).andReturn(path).atLeastOnce();
        EasyMock.expect(mock.getServerName()).andReturn("example.com").atLeastOnce();
        EasyMock.expect(mock.getQueryString()).andReturn("").atLeastOnce();
        EasyMock.expect(mock.getServerPort()).andReturn(443).atLeastOnce();
        EasyMock.replay(mock);
        return mock;
    }

    private static byte[] invalidUtf8 = flatten(new byte[][] {
        "<!DOCTYPE html><head><title>1".getBytes(),
        new byte[]{ 65 }, // A
        "z</title></head><body><h1>2".getBytes(),
        new byte[]{ -126 }, // invalid utf8
        "z</h1><h2>3".getBytes(),
        new byte[]{ -62, -126 }, // valid utf8 but this is controll charactor
        "z</h2></body></html>".getBytes()
    });

    private static byte[] flatten(byte[][] src) {
        int sum = 0;
        for (int i=0; i<src.length; ++i) {
            sum += src[i].length;
        }
        int offset = 0;
        byte[] dst = new byte[sum];
        for (int i=0; i<src.length; ++i) {
            byte[] bytes = src[i];
            int size = bytes.length;
            System.arraycopy(bytes, 0, dst, offset, size);
            offset += size;
        }
        return dst;
    }
}
