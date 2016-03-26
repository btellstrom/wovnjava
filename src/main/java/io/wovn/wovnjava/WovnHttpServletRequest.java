package io.wovn.wovnjava;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class WovnHttpServletRequest extends HttpServletRequestWrapper {
    private Headers headers;

    public WovnHttpServletRequest(HttpServletRequest r, Headers h) {
        super(r);
        headers = h;
    }

    public String getRemoteHost() {
        String host = super.getRemoteHost();
        if (headers.settings.urlPattern.equals("subdomain")) {
            host = headers.removeLang(host, null);
        }
        return host;
    }

    public String getServerName() {
        String serverName = super.getServerName();
        if (headers.settings.urlPattern.equals("sudomain")) {
            serverName = headers.removeLang(serverName, null);
        }
        return serverName;
    }

    public String getRequestURI() {
        String uri = super.getRequestURI();
        if (!headers.settings.urlPattern.equals("subdomain")) {
            if (uri != null && uri.length() > 0) {
                uri = headers.removeLang(uri, null);
            }
        }
        return uri;
    }
}
