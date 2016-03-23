package io.wovn.wovnjava;

import java.io.*;

import javax.servlet.*;

public class FilterServletOutputStream extends ServletOutputStream {
    protected OutputStream stream;

    public FilterServletOutputStream(OutputStream stream) {
        this.stream = stream;
    }

    @Override
    public void write(int b) throws IOException  {
        stream.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException  {
        stream.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException  {
        stream.write(b,off,len);
    }

    @Override
    public void close() throws java.io.IOException {
        stream.close();
    }

    @Override
    public void flush() throws IOException {
        stream.flush();
    }
}
