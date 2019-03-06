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

    public static final String VERSION = Settings.VERSION;  // for backward compatibility

    @Override
    public void init(FilterConfig config) throws ServletException {
        this.settings = new Settings(config);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException
    {
        ((HttpServletResponse)response).setHeader("X-Wovn-Top", "init");
        Headers headers = new Headers((HttpServletRequest)request, settings);
        String lang = headers.getPathLang();
        boolean hasShorterPath = settings.urlPattern.equals("path") && lang.length() > 0 && lang.equals(settings.defaultLang);
        if (hasShorterPath) {
            ((HttpServletResponse)response).setHeader("X-Wovn-Top", "redirect!");
            ((HttpServletResponse) response).sendRedirect(headers.redirectLocation(settings.defaultLang));
        } else if (htmlChecker.canTranslatePath(headers.pathName)) {
            ((HttpServletResponse)response).setHeader("X-Wovn-Top", "try-trans!");
            tryTranslate(headers, (HttpServletRequest)request, response, chain);
        } else {
            ((HttpServletResponse)response).setHeader("X-Wovn-Top", "pass!");
            WovnHttpServletRequest wovnRequest = new WovnHttpServletRequest((HttpServletRequest)request, headers);
            chain.doFilter(wovnRequest, response);
        }
    }

    @Override
    public void destroy() {
    }

    private void tryTranslate(Headers headers, HttpServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ((HttpServletResponse)response).setHeader("X-Wovn-Trans", "init");
        WovnHttpServletRequest wovnRequest = new WovnHttpServletRequest(request, headers);
        WovnHttpServletResponse wovnResponse = new WovnHttpServletResponse((HttpServletResponse)response, headers);

        if (settings.urlPattern.equals("path") && headers.getPathLang().length() > 0) {
            ((HttpServletResponse)response).setHeader("X-Wovn-Trans", "forward");
            ((HttpServletResponse)response).setHeader("X-Wovn-Forward", headers.pathNameKeepTrailingSlash);
            headers.trace("WovnServletFilter#forward to " + headers.pathNameKeepTrailingSlash);
            wovnRequest.getRequestDispatcher(headers.pathNameKeepTrailingSlash).forward(wovnRequest, wovnResponse);
        } else {
            ((HttpServletResponse)response).setHeader("X-Wovn-Trans", "call-chain");
            headers.trace("WovnServletFilter#chain.doFilter");
            chain.doFilter(wovnRequest, wovnResponse);
        }
        ((HttpServletResponse)response).setHeader("X-Wovn-Trans", "response-received");

        headers.trace("WovnServletFilter#Response received");
        String originalBody = wovnResponse.toString();
        if (originalBody != null) {
            ((HttpServletResponse)response).setHeader("X-Wovn-Trans", "is-text");
            // text
            String body = null;
            if (htmlChecker.canTranslate(response.getContentType(), headers.pathName, originalBody)) {
                ((HttpServletResponse)response).setHeader("X-Wovn-Trans", "text-can-translate");
                // html
                Api api = new Api(settings, headers);
                ((HttpServletResponse)response).setHeader("X-Wovn-Trans", "text-api");
                Interceptor interceptor = new Interceptor(headers, settings, api);
                body = interceptor.translate(originalBody);
                ((HttpServletResponse)response).setHeader("X-Wovn-Trans", "text-intercepted");

                // Temporary debugging section
                headers.trace("WovnServletFilter#translation complete");
                String debugging_section = createDebuggingSection(request, wovnResponse, headers);
                body = body + debugging_section;
            } else {
                ((HttpServletResponse)response).setHeader("X-Wovn-Trans", "no-text");
                // css, javascript or others
                body = originalBody;
            }

            ((HttpServletResponse)response).setHeader("X-Wovn-Trans", "text-body-ready");
            wovnResponse.setContentLength(body.getBytes().length);
            wovnResponse.setCharacterEncoding("utf-8");
            ((HttpServletResponse)response).setHeader("X-Wovn-Trans", "text-writer-fetching");
            PrintWriter out = response.getWriter();
            ((HttpServletResponse)response).setHeader("X-Wovn-Trans", "text-write!");
            out.write(body);
            out.close();
        } else {
            ((HttpServletResponse)response).setHeader("X-Wovn-Trans", "is-binary");
            // binary
            ServletOutputStream out = response.getOutputStream();
            ((HttpServletResponse)response).setHeader("X-Wovn-Trans", "binary-write!");
            out.write(wovnResponse.getData());
            out.close();
        }
    }

    private String createDebuggingSection(HttpServletRequest request, WovnHttpServletResponse response, Headers headers) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n<!--Debugging information:");
        sb.append("\nVersion=" + VERSION);
        sb.append("\nTimestamp=" + System.currentTimeMillis());
        sb.append("\nlangCode=" + headers.langCode());
        sb.append("\ngetRequestURI=" + request.getRequestURI());
        sb.append("\nforward.request_uri=" + request.getAttribute("javax.servlet.forward.request_uri"));
        sb.append("\ngetQueryString=" + request.getQueryString());
        sb.append("\noriginal_encoding=" + response.original_encoding);
        sb.append("\n===trace===\n" + headers.printTrace());
        sb.append("-->\n");
        return sb.toString();
    }
}
