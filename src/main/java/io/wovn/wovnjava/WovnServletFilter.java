package io.wovn.wovnjava;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

public class WovnServletFilter implements Filter {
    protected Interceptor interceptor;

    public static final String VERSION = "1.0";

    public void init(FilterConfig config) throws ServletException {
        interceptor = new Interceptor(config);
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException
    {
        HttpServletRequest r = (HttpServletRequest) request;
        interceptor.call(r, response, chain);
    }

    public void destroy() {
    }
}
