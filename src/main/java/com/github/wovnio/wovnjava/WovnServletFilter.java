package com.github.wovnio.wovnjava;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WovnServletFilter implements Filter {
    //protected Interceptor interceptor;
    private Settings settings;

    public static final String VERSION = "0.3.0";

    public void init(FilterConfig config) throws ServletException {
        this.settings = new Settings(config);
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException
    {
        Headers headers = new Headers((HttpServletRequest)request, settings);
        String lang = headers.langCode();
        boolean hasShorterPath = lang.length() > 0 && lang.equals(settings.defaultLang);

        if (!isHtml(headers.pathName)) {
            chain.doFilter(request, response);
        } else if (hasShorterPath) {
            ((HttpServletResponse) response).sendRedirect(headers.redirectLocation(settings.defaultLang));
        } else {
            tryTranslate(headers, (HttpServletRequest)request, response, chain);
        }
    }

    public void destroy() {
    }

    boolean isHtml(String path) {
        // TODO: implement
        return true;
    }

    private void tryTranslate(Headers headers, HttpServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        WovnHttpServletRequest wovnRequest = new WovnHttpServletRequest(request, headers);
        WovnHttpServletResponse wovnResponse = new WovnHttpServletResponse((HttpServletResponse)response);
        chain.doFilter(wovnRequest, wovnResponse);
        String originalBody = wovnResponse.toString();
        if (originalBody != null) {
            // text
            API api = new API(VERSION, headers, settings);
            Interceptor interceptor = new Interceptor(VERSION, headers, settings, api);
            String body = interceptor.translate(originalBody);
            wovnResponse.setContentLength(body.getBytes().length);
            wovnResponse.setContentType("text/html; charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(body);
            out.close();
        } else {
            // binary
            ServletOutputStream out = response.getOutputStream();
            out.write(wovnResponse.getData());
            out.close();
        }
        headers.out(wovnRequest, wovnResponse);
    }
}
