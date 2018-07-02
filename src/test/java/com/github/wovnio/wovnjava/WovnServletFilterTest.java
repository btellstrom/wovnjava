package com.github.wovnio.wovnjava;

import java.io.IOException;
import javax.servlet.FilterConfig;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.easymock.EasyMock;


public class WovnServletFilterTest extends TestCase {

    public void testHtml() throws ServletException, IOException {
        FilterChainMock mock = TestUtil.doServletFilter("text/html; charset=utf-8", "/");
        assertEquals("text/html; charset=utf-8", mock.res.getContentType());
        assertEquals("/", mock.req.getRequestURI());
    }

    public void testHtmlWithLang() throws ServletException, IOException {
        FilterChainMock mock = TestUtil.doServletFilter("text/html; charset=utf-8", "/ja/", "/");
        assertEquals("text/html; charset=utf-8", mock.res.getContentType());
        assertEquals("/", mock.req.getRequestURI());
    }

    public void testCss() throws ServletException, IOException {
        FilterChainMock mock = TestUtil.doServletFilter("text/css", "/dir/style.css");
        assertEquals("text/css", mock.res.getContentType());
        assertEquals("/dir/style.css", mock.req.getRequestURI());
    }

    public void testCssWithLang() throws ServletException, IOException {
        FilterChainMock mock = TestUtil.doServletFilter("text/css", "/ja/style.css", "/style.css");
        assertEquals("text/css", mock.res.getContentType());
        assertEquals("/style.css", mock.req.getRequestURI());
    }

    public void testImage() throws ServletException, IOException {
        FilterChainMock mock = TestUtil.doServletFilter("image/png", "/image.png");
        assertEquals("image/png", mock.res.getContentType());
        assertEquals("/image.png", mock.req.getRequestURI());
    }

    public void testImageWithLang() throws ServletException, IOException {
        FilterChainMock mock = TestUtil.doServletFilter("image/png", "/ja/image.png", "image.png");
        assertEquals("image/png", mock.res.getContentType());
        assertEquals("/image.png", mock.req.getRequestURI());
    }
}
