package com.github.wovnio.wovnjava;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class FilterChainMock implements FilterChain {
    public HttpServletRequest req;
    public ServletResponse res;

    public void doFilter(ServletRequest req, ServletResponse res) {
        this.req = (HttpServletRequest)req;
        this.res = res;
    }
}
