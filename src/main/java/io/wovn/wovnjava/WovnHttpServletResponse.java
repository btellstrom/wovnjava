package io.wovn.wovnjava;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

class WovnHttpServletResponse extends HttpServletResponseWrapper {
    int status;

    private Headers headers;
    private ByteArrayOutputStream buff;
    private PrintWriter writer;
    private ServletOutputStream output;

    WovnHttpServletResponse(HttpServletResponse response, Headers h) {
        super(response);
        this.headers = h;
        this.buff = new ByteArrayOutputStream();
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
