package com.github.wovnio.wovnjava;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

class WovnHttpServletResponse extends HttpServletResponseWrapper {
    int status;

    private ByteArrayOutputStream buff;
    private PrintWriter writer;
    private ServletOutputStream output;
    private Headers headers;

    WovnHttpServletResponse(HttpServletResponse response, Headers headers) {
        super(response);
        this.buff = new ByteArrayOutputStream();
        this.headers = headers;
    }

    byte[] getData() {
        if (this.writer != null) {
            this.writer.close();
        }
        if (this.output != null) {
            try {
                this.output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        byte[] ret = buff.toByteArray();

        this.buff = new ByteArrayOutputStream();
        this.output = null;
        this.writer = null;

        return ret;
    }

    public String toString() {
        return Utf8.toStringUtf8(this.getData());
    }

    public void setStatus(int sc) {
        status = sc;
        super.setStatus(sc);
    }

    public ServletOutputStream getOutputStream() throws IOException {
        if (this.output == null)
            this.output = new FilterServletOutputStream(this.buff);

        return this.output;
    }

    public PrintWriter getWriter() throws IOException {
        if (this.writer == null) {
            this.writer = new PrintWriter(
                    new OutputStreamWriter(this.getOutputStream(), this.getCharacterEncoding()),
                    true
            );
        }
        return this.writer;
    }

    public void sendRedirect(String location) throws java.io.IOException {
        super.sendRedirect(locationWithLangCode(location));
    }

    public void setHeader(String name, String value) {
        if (name.toLowerCase() == "location") {
            value = locationWithLangCode(value);
        }
        super.setHeader(name, value);
    }

    private String locationWithLangCode(String loc) {
        if (loc == null) {
            return null;
        }

        // capture protocol if location included
        String protocol = headers.protocol;
        if (loc.contains("//")) {
            if (!loc.contains("//" + headers.host)) {
                return loc;
            }
            protocol = loc.split("//")[0];
        }

        String langCode = headers.langCode();
        String queryLang = "";
        String subdomainLang = "";
        String pathLang = "";
        if (headers.settings.urlPattern.equals("query")) {
            if (loc.contains("?")) {
                queryLang = "&wovn=" + langCode;
            } else {
                queryLang = "?wovn=" + langCode;
            }
        } else if (headers.settings.urlPattern.equals("subdomain")) {
            subdomainLang = langCode + ".";
        } else {
            pathLang = langCode;
        }
        String pathAll = pathJoin(pathLang, pathJoin(headers.pathNameKeepTrailingSlash, loc));
        return protocol + "://" + subdomainLang + headers.host + pathAll + queryLang;
    }

    private String pathJoin(String left, String right) {
        int slash_pos = left.lastIndexOf("/");
        left = slash_pos >= 0 ? left.substring(0, slash_pos) : "";
        boolean l = left.endsWith("/");
        boolean r = right.startsWith("/");
        if (l && r) {
            return left + right.substring(1);
        } else if (l || r) {
            return left + right;
        } else {
            return left + "/" + right;
        }
    }
}
