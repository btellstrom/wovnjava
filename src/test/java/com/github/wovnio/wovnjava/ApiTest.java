package com.github.wovnio.wovnjava;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.easymock.EasyMock;


public class ApiTest extends TestCase {

    public void testNormal() throws ApiException, IOException, ProtocolException {
        Settings settings = TestUtil.makeSettings(new HashMap<String, String>() {{
            put("projectToken", "token0");
            put("defaultLang", "en");
            put("supportedLangs", "en,ja,fr");
        }});
        HttpServletRequest request = TestUtil.mockRequestPath("/ja/");
        String html = translate(request, settings, "<html></html>", 200, "gzip", gzip("{\"body\": \"response html\"}".getBytes()));
        String expect = "response html";
        assertEquals(expect, html);
    }

    public void testWithPlainTextResponse() throws ApiException, IOException, ProtocolException {
        Settings settings = TestUtil.makeSettings(new HashMap<String, String>() {{
            put("projectToken", "token0");
            put("defaultLang", "en");
            put("supportedLangs", "en,ja,fr");
        }});
        HttpServletRequest request = TestUtil.mockRequestPath("/ja/");
        String html = translate(request, settings, "<html></html>", 200, "", "{\"body\": \"response html\"}".getBytes());
        String expect = "response html";
        assertEquals(expect, html);
    }

    private String translate(HttpServletRequest request, Settings settings, String html, int code, String encoding, byte[] response) throws ApiException, IOException, ProtocolException {
        Headers headers = new Headers(request, settings);
        HttpURLConnection con = mockHttpURLConnection(gzip(html.getBytes()).length, code, encoding, response);
        Api api = new Api(settings, headers);
        return api.translate("ja", html, con);
    }

    private byte[] gzip(byte[] input) throws IOException, ProtocolException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream(input.length);
        GZIPOutputStream gz = new GZIPOutputStream(buffer);
        try {
            gz.write(input);
        } finally {
            gz.close();
        }
        return buffer.toByteArray();
    }

    private HttpURLConnection mockHttpURLConnection( int sendLength, int code, String encoding, byte[] response) throws IOException, ProtocolException {
        InputStream responseStream = new ByteArrayInputStream(response);
        HttpURLConnection mock = EasyMock.createMock(HttpURLConnection.class);
        mock.setDoOutput(true);
        mock.setRequestProperty(EasyMock.anyString(), EasyMock.anyString());
        EasyMock.expectLastCall().atLeastOnce();
        mock.setRequestMethod("POST");
        EasyMock.expect(mock.getResponseCode()).andReturn(code);
        EasyMock.expect(mock.getContentEncoding()).andReturn(encoding);
        EasyMock.expect(mock.getOutputStream()).andReturn(new ByteArrayOutputStream());
        EasyMock.expect(mock.getInputStream()).andReturn(responseStream);
        EasyMock.replay(mock);
        return mock;
    }
}
