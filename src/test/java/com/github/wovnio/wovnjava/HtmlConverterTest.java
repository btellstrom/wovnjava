package com.github.wovnio.wovnjava;

import java.util.HashMap;

import javax.servlet.FilterConfig;

import junit.framework.TestCase;

import org.easymock.EasyMock;

public class HtmlConverterTest extends TestCase {

    public void testRemoveWovnSnippet() {
        String original = "<html><head><script src=\"//j.wovn.io/1\" data-wovnio=\"key=NCmbvk&amp;backend=true&amp;currentLang=en&amp;defaultLang=en&amp;urlPattern=path&amp;langCodeAliases={}&amp;version=0.3.0\" data-wovnio-type=\"backend_without_api\" async></script></head><body></body></html>";
        String removedHtml = "<html><head></head><body></body></html>";
        Settings settings = makeSettings(new HashMap<String, String>() {{ put("supportedLangs", "en,fr,ja"); }});
        HtmlConverter converter = new HtmlConverter(settings, original);
        String html = converter.strip();
        assertEquals(removedHtml, stripExtraSpaces(html));
        assertEquals(removedHtml, stripExtraSpaces(converter.restore(html)));
    }

    public void testRemoveScripts() {
        String original = "<html><head><script>alert(1)</script></head><body>a <script>console.log(1)</script>b</body></html>";
        String removedHtml = "<html><head><!--wovn-marker-0--></head><body>a <!--wovn-marker-1-->b</body></html>";
        Settings settings = makeSettings(new HashMap<String, String>() {{ put("supportedLangs", "en,fr,ja"); }});
        HtmlConverter converter = new HtmlConverter(settings, original);
        String html = converter.strip();
        assertEquals(removedHtml, stripExtraSpaces(html));
        assertEquals(stripExtraSpaces(original), stripExtraSpaces(converter.restore(html)));
    }

    public void testRemoveHrefLangIfConflicts() {
        String original = "<html><head><link ref=\"altername\" hreflang=\"en\" href=\"http://localhost:8080/\"><link ref=\"altername\" hreflang=\"ja\" href=\"http://localhost:8080/ja/\"><link ref=\"altername\" hreflang=\"ar\" href=\"http://localhost:8080/ar/\"></head><body></body></html>";
        String removedHtml = "<html><head><link ref=\"altername\" hreflang=\"ar\" href=\"http://localhost:8080/ar/\"></head><body></body></html>";
        Settings settings = makeSettings(new HashMap<String, String>() {{ put("supportedLangs", "en,fr,ja"); }});
        HtmlConverter converter = new HtmlConverter(settings, original);
        String html = converter.strip();
        assertEquals(removedHtml, stripExtraSpaces(html));
        assertEquals(removedHtml, stripExtraSpaces(converter.restore(html)));
    }

    public void testRemoveWovnIgnore() {
        String original = "<html><head></head><body><div>Hello <span wovn-ignore>Duke</span>.</div></body></html>";
        String removedHtml = "<html><head></head><body><div>Hello <!--wovn-marker-0-->.</div></body></html>";
        Settings settings = makeSettings(new HashMap<String, String>() {{ put("supportedLangs", "en,fr,ja"); }});
        HtmlConverter converter = new HtmlConverter(settings, original);
        String html = converter.strip();
        assertEquals(removedHtml, stripExtraSpaces(html));
        assertEquals(original, stripExtraSpaces(converter.restore(html)));
    }

    public void testRemoveForm() {
        String original = "<html><head></head><body><form><input type=\"hidden\" name=\"csrf\" value=\"random\"><INPUT TYPE=\"HIDDEN\" NAME=\"CSRF_TOKEN\" VALUE=\"RANDOM\"></form></body></html>";
        String removedHtml = "<html><head></head><body><form><!--wovn-marker-0--><!--wovn-marker-1--></form></body></html>";
        Settings settings = makeSettings(new HashMap<String, String>() {{ put("supportedLangs", "en,fr,ja"); }});
        HtmlConverter converter = new HtmlConverter(settings, original);
        String html = converter.strip();
        assertEquals(removedHtml, stripExtraSpaces(html));
        // jsoup make lower case tag name
        assertEquals(original.replace("INPUT", "input"), stripExtraSpaces(converter.restore(html)));
    }

    public void testNested() {
        String original = "<html><head></head><body><form wovn-ignore><script></script><input type=\"hidden\" name=\"csrf\" value=\"random\"><INPUT TYPE=\"HIDDEN\" NAME=\"CSRF_TOKEN\" VALUE=\"RANDOM\"></form></body></html>";
        String removedHtml = "<html><head></head><body><!--wovn-marker-1--></body></html>";
        Settings settings = makeSettings(new HashMap<String, String>() {{ put("supportedLangs", "en,fr,ja"); }});
        HtmlConverter converter = new HtmlConverter(settings, original);
        String html = converter.strip();
        assertEquals(removedHtml, stripExtraSpaces(html));
        // jsoup make lower case tag name
        assertEquals(original.replace("INPUT", "input"), stripExtraSpaces(converter.restore(html)));
    }

    public void testMixAllCase() {
        String original = "<html><head>" +
            "<script src=\"//j.wovn.io/1\" data-wovnio=\"key=NCmbvk&amp;backend=true&amp;currentLang=en&amp;defaultLang=en&amp;urlPattern=path&amp;langCodeAliases={}&amp;version=0.3.0\" data-wovnio-type=\"backend_without_api\" async></script>" +
            "<script>alert(1)</script>" +
            "<link ref=\"altername\" hreflang=\"en\" href=\"http://localhost:8080/\"><link ref=\"altername\" hreflang=\"ja\" href=\"http://localhost:8080/ja/\"><link ref=\"altername\" hreflang=\"ar\" href=\"http://localhost:8080/ar/\">" +
            "</head><body>" +
            "a <script>console.log(1)</script>b" +
            "<div>Hello <span wovn-ignore>Duke</span>.</div>" +
            "<form><input type=\"hidden\" name=\"csrf\" value=\"random\"></form>" +
            "<script>4</script>" +
            "<script>5</script>" +
            "<script>6</script>" +
            "<script>7</script>" +
            "<script>8</script>" +
            "<script>9</script>" +
            "<script>10</script>" +
            "</body></html>";
        String removedHtml = "<html><head>" +
            "<!--wovn-marker-0-->" +
            "<link ref=\"altername\" hreflang=\"ar\" href=\"http://localhost:8080/ar/\">" +
            "</head><body>" +
            "a <!--wovn-marker-1-->b" +
            "<div>Hello <!--wovn-marker-9-->.</div>" +
            "<form><!--wovn-marker-10--></form>" +
            "<!--wovn-marker-2-->" +
            "<!--wovn-marker-3-->" +
            "<!--wovn-marker-4-->" +
            "<!--wovn-marker-5-->" +
            "<!--wovn-marker-6-->" +
            "<!--wovn-marker-7-->" +
            "<!--wovn-marker-8-->" +
            "</body></html>";
        Settings settings = makeSettings(new HashMap<String, String>() {{ put("supportedLangs", "en,fr,ja"); }});
        HtmlConverter converter = new HtmlConverter(settings, original);
        String html = converter.strip();
        assertEquals(removedHtml, stripExtraSpaces(html));
    }

    private Settings makeSettings(HashMap<String, String> option) {
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

    private String stripExtraSpaces(String html) {
        return html.replaceAll("\\s +", "").replaceAll(">\\s+<", "><");
    }
}
