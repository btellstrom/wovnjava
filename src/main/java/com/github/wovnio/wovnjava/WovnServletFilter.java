package com.github.wovnio.wovnjava;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WovnServletFilter implements Filter {
    protected Interceptor interceptor;

    public static final String VERSION = "0.2.1";

    public void init(FilterConfig config) throws ServletException {
        interceptor = new Interceptor(config);
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException
    {
        interceptor.call((HttpServletRequest) request, response, chain);
    }

    public void destroy() {
    }
}
