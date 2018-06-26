package com.github.wovnio.wovnjava;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestDispatcherMock implements RequestDispatcher {
    public HttpServletRequest req;
    public HttpServletResponse res;

    public void forward(ServletRequest req, ServletResponse res) {
        this.req = (HttpServletRequest)req;
        this.res = (HttpServletResponse)res;
    }

    public void include(ServletRequest req, ServletResponse res) {
        this.req = (HttpServletRequest)req;
        this.res = (HttpServletResponse)res;
    }
}
