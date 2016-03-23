package io.wovn.wovnjava;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

public class WovnHttpServletResponse extends HttpServletResponseWrapper {
    public int status;

    protected Headers headers;
    protected ByteArrayOutputStream buff;
    protected PrintWriter writer;
    protected ServletOutputStream output;

    public WovnHttpServletResponse(HttpServletResponse response, Headers h) {
        super(response);
        this.headers = h;
        this.buff = new ByteArrayOutputStream();
    }

    public byte[] getData() {
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
        return new String(this.getData());
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
                    new OutputStreamWriter(this.getOutputStream(),this.getCharacterEncoding()),
                    true
            );
        }
        return this.writer;
    }
}
