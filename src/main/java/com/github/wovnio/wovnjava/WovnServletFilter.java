package com.github.wovnio.wovnjava;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static final String VERSION = Settings.VERSION;  // for backword compatibility

    public void init(FilterConfig config) throws ServletException {
        this.settings = new Settings(config);
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException
    {
        Headers headers = new Headers((HttpServletRequest)request, settings);
        String lang = headers.getPathLang();
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
        if (originalBody != null && (!settings.strictHtmlCheck || isHtmlContent(originalBody))) {
            // text
            Api api = new Api(settings, headers);
            Interceptor interceptor = new Interceptor(headers, settings, api);
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

    private boolean isHtmlContent(String body) {
        if (Logger.isDebug()) {
            Logger.log.info("Checking HTML strictly.");

            if (Logger.debugMode > 1) {
                Logger.log.info("original HTML:\n" + body);
            }
        }

        // Remove comments.
        body = Pattern.compile("(?m)\\A(\\s*<!--[\\s\\S]*?-->\\s*)+").matcher(body).replaceAll("");

        // Remove spaces.
        body = Pattern.compile("(?m)\\A\\s+").matcher(body).replaceAll("");

        if (Logger.debugMode > 1) {
            Logger.log.info("HTML after removing comment tags and spaces:\n" + body);
        }

        if (Pattern.compile("(?m)\\A<\\?xml\\b", Pattern.CASE_INSENSITIVE).matcher(body).find()             // <?xml
                || Pattern.compile("(?m)\\A<!DOCTYPE\\b", Pattern.CASE_INSENSITIVE).matcher(body).find()    // <!DOCTYPE
                || Pattern.compile("(?m)\\A<html\\b", Pattern.CASE_INSENSITIVE).matcher(body).find()        // <html
                ) {
            if (Logger.isDebug()) {
                Logger.log.info("This data is HTML.");
            }
            return true;
        } else {
            if (Logger.isDebug()) {
                Logger.log.info("This data is not HTML.");
            }
            return false;
        }
    }
}
