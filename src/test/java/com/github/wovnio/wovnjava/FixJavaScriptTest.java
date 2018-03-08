package com.github.wovnio.wovnjava;

import junit.framework.TestCase;


public class FixJavaScriptTest extends TestCase {
    public void testEscapeJavaScript() {
        String html = "<!DOCTYPE html>"
            + "<html>"
            + "<head>"
            + "<script></script>"
            + "<script src=\"1.js\"></script>"
            + "<script src=\"1.js\" async></script>"
            + "<script type=\"text/javascript\">alert(1)</script>"
            + "<script>alert(1)</script>"
            + "<script>\nalert(1)\n</script>"
            + "</head>"
            + "<body>"
            + "hello"
            + "<script>\nalert(1)\nconsole.log(2)\n</script>"
            + "world"
            + "</body>"
            + "</html>";
        String expect = "<!DOCTYPE html>"
            + "<html>"
            + "<head>"
            + "<script>/* WOVNJAVA-SCRIPT-TAG-MARKER */</script>"
            + "<script>/* WOVNJAVA-SCRIPT-TAG-MARKER */</script>"
            + "<script>/* WOVNJAVA-SCRIPT-TAG-MARKER */</script>"
            + "<script>/* WOVNJAVA-SCRIPT-TAG-MARKER */</script>"
            + "<script>/* WOVNJAVA-SCRIPT-TAG-MARKER */</script>"
            + "<script>/* WOVNJAVA-SCRIPT-TAG-MARKER */</script>"
            + "</head>"
            + "<body>"
            + "hello"
            + "<script>/* WOVNJAVA-SCRIPT-TAG-MARKER */</script>"
            + "world"
            + "</body>"
            + "</html>";
        FixJavaScript fjs = new FixJavaScript(html);
        String escapedHtml = fjs.escape();
        assertEquals(expect, escapedHtml);
        assertEquals(html, fjs.unescape(escapedHtml));
    }

    public void testNoJavaScript() {
        String html = "<!DOCTYPE html>"
            + "<html>"
            + "<head>"
            + "</head>"
            + "<body>"
            + "hello"
            + "world"
            + "</body>"
            + "</html>";
        FixJavaScript fjs = new FixJavaScript(html);
        String escapedHtml = fjs.escape();
        assertEquals(html, escapedHtml);
        assertEquals(html, fjs.unescape(escapedHtml));
    }
}
