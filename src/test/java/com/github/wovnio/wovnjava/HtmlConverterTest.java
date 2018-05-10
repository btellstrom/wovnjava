package com.github.wovnio.wovnjava;

import junit.framework.TestCase;

public class HtmlConverterTest extends TestCase {
    public void testStripScripts() {
        String original = "a<script>...</script>b";
        HtmlConverter converter = new HtmlConverter(original);
        String html = converter.strip();
        //assertEquals("a...b", html);
        assertEquals(original, converter.restore(html));
    }
}
