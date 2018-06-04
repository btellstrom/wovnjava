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
    private Settings settings;
    private final HtmlChecker htmlChecker = new HtmlChecker();

    public static final String VERSION = Settings.VERSION;  // for backword compatibility

    public void init(FilterConfig config) throws ServletException {
        this.settings = new Settings(config);
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException
    {
        Headers headers = new Headers((HttpServletRequest)request, settings);
        String lang = headers.getPathLang();
        boolean hasLang = lang.length() > 0;
        boolean hasShorterPath = hasLang && lang.equals(settings.defaultLang);
        if (hasShorterPath) {
            ((HttpServletResponse) response).sendRedirect(headers.redirectLocation(settings.defaultLang));
        } else if(hasLang) {
            tryTranslate(headers, (HttpServletRequest)request, response, chain);
        } else {
            chain.doFilter(request, response);
        }
    }

    public void destroy() {
    }

    private void tryTranslate(Headers headers, HttpServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        WovnHttpServletRequest wovnRequest = new WovnHttpServletRequest(request, headers);
        WovnHttpServletResponse wovnResponse = new WovnHttpServletResponse((HttpServletResponse)response);

        wovnRequest.getRequestDispatcher(headers.pathNameKeepTrailingSlash).forward(wovnRequest, wovnResponse);

        String originalBody = wovnResponse.toString();
        if (originalBody != null) {
            // text
            String body = null;
            if (htmlChecker.canTranslate(response.getContentType(), headers.pathName, originalBody)) {
                // html
                Api api = new Api(settings, headers);
                Interceptor interceptor = new Interceptor(headers, settings, api);
                body = interceptor.translate(originalBody);
            } else {
                // css, javascript or others
                body = originalBody;
            }
            wovnResponse.setContentLength(body.getBytes().length);
            wovnResponse.setCharacterEncoding("utf-8");
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
