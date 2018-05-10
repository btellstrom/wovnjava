package com.github.wovnio.wovnjava;

import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.net.MalformedURLException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.LinkedHashMap;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

import net.arnx.jsonic.JSON;

class Api {
    private final String version;
    private final Settings settings;
    private final Headers headers;
    private final String responseEncoding = "UTF-8"; // always response is UTF8

    Api(String version, Headers headers, Settings settings) {
        this.version = version;
        this.headers = headers;
        this.settings = settings;
    }

    String translate(String lang, String body) throws ApiException {
        HttpURLConnection con = null;
        try {
            URL url = getApiUrl(lang, body);
            byte[] data = getApiBody(lang, body).getBytes();

            con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Accept-Encoding", "gzip");
            con.setRequestProperty("Content-Type", "application/octet-stream");
            con.setRequestProperty("Content-Length", String.valueOf(data.length));
            con.setDoOutput(true);
            con.setRequestMethod("POST");

            writeGzip(con.getOutputStream(), data);

            con.connect();
            int status = con.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                InputStream input = con.getInputStream();
                if ("gzip".equals(con.getContentEncoding())) {
                    input = new GZIPInputStream(input);
                }
                return extractHtml(input);
            } else {
                throw new ApiException("status_" + String.valueOf(status));
            }
        } catch (Exception e) {
            Logger.log.error("Api", e);
            throw new ApiException("unknown");
        } finally {
            con.disconnect();
        }
    }

    private void writeGzip(OutputStream out, byte[] input) throws IOException, UnsupportedEncodingException {
        GZIPOutputStream gz = new GZIPOutputStream(out);
        try {
            gz.write(input);
        } finally {
            gz.close();
        }
    }

    private String extractHtml(InputStream input) throws ApiException, IOException, UnsupportedEncodingException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[8 * 1024];
        int len = 0;
        while ((len = input.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
        input.close();

        String json = out.toString(responseEncoding);
        LinkedHashMap<String, String> dict = JSON.decode(json);
        String html = dict.get("body");
        if (html == null) {
            Logger.log.error("Unknown json format " + json);
            throw new ApiException("unknown_json_format");
        }
        return html;
    }

    private String getApiBody(String lang, String body) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
		appendField(sb, "url", headers.url);
		appendField(sb, "token", settings.projectToken);
		appendField(sb, "lang_code", lang);
		appendField(sb, "url_pattern", settings.urlPattern);
		appendField(sb, "body", body);
        return sb.toString();
    }

	private void appendField(StringBuilder sb, String key, String value) throws UnsupportedEncodingException {
        sb.append(URLEncoder.encode(key, "UTF-8"));
		sb.append('=');
        sb.append(URLEncoder.encode(value, "UTF-8"));
		sb.append('&');
    }

    private URL getApiUrl(String lang, String body) throws UnsupportedEncodingException, NoSuchAlgorithmException, MalformedURLException {
        StringBuilder sb = new StringBuilder();
        sb.append(settings.apiUrl);
        sb.append("translation?cache_key=");
        sb.append(URLEncoder.encode("(token=", "UTF-8"));
        sb.append(URLEncoder.encode(settings.projectToken, "UTF-8"));
        sb.append(URLEncoder.encode("&settings_hash=", "UTF-8"));
        sb.append(URLEncoder.encode(settings.hash(), "UTF-8"));
        sb.append(URLEncoder.encode("&body_hash=", "UTF-8"));
        sb.append(URLEncoder.encode(hash(body.getBytes()), "UTF-8"));
        sb.append(URLEncoder.encode("&path=", "UTF-8"));
        sb.append(URLEncoder.encode(headers.pathName, "UTF-8"));
        sb.append(URLEncoder.encode("&lang=", "UTF-8"));
        sb.append(URLEncoder.encode(lang, "UTF-8"));
        sb.append(URLEncoder.encode("&version=", "UTF-8"));
        sb.append(URLEncoder.encode(version, "UTF-8"));
        sb.append(URLEncoder.encode(")", "UTF-8"));
        return new URL(sb.toString());
    }

    private String hash(byte[] item) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(item);
        byte[] digest = md.digest();
        return DatatypeConverter.printHexBinary(digest).toUpperCase();
    }
}
