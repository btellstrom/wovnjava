package com.github.wovnio.wovnjava;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.HttpURLConnection;
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
    private final int READ_BUFFER_SIZE = 8196;
    private final Settings settings;
    private final Headers headers;
    private final String responseEncoding = "UTF-8"; // always response is UTF8

    Api(Settings settings, Headers headers) {
        this.settings = settings;
        this.headers = headers;
    }

    String translate(String lang, String html) throws ApiException {
        headers.trace("Api#translate(1)");
        HttpURLConnection con = null;
        try {
            URL url = getApiUrl(lang, html);
            con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(settings.connectTimeout);
            con.setReadTimeout(settings.readTimeout);
            return translate(lang, html, con);
        } catch (UnsupportedEncodingException e) {
            headers.trace("Api#UnsupportedEncodingException(1): " + e);
            Logger.log.error("Api url", e);
            throw new ApiException("encoding");
        } catch (IOException e) {
            headers.trace("Api#IOException(1): " + e);
            Logger.log.error("Api url", e);
            throw new ApiException("io");
        } catch (NoSuchAlgorithmException e) {
            headers.trace("Api#NoSuchAlgorithmException: " + e);
            Logger.log.error("Api url", e);
            throw new ApiException("algorithm");
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

    String translate(String lang, String html, HttpURLConnection con) throws ApiException {
        headers.trace("Api#translate(2)");
        OutputStream out = null;
        try {
            ByteArrayOutputStream body = gzipStream(getApiBody(lang, html).getBytes());
            con.setDoOutput(true);
            con.setRequestProperty("Accept-Encoding", "gzip");
            con.setRequestProperty("Content-Type", "application/octet-stream");
            con.setRequestProperty("Content-Length", String.valueOf(body.size()));
            con.setRequestMethod("POST");
            out = con.getOutputStream();
            body.writeTo(out);
            out.close();
            out = null;
            int status = con.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                headers.trace("Api#HttpURLConnection.HTTP_OK");
                InputStream input = con.getInputStream();
                if ("gzip".equals(con.getContentEncoding())) {
                    input = new GZIPInputStream(input);
                }
                return extractHtml(input);
            } else {
                headers.trace("Api#ApiException " + String.valueOf(status));
                throw new ApiException("status_" + String.valueOf(status));
            }
        } catch (UnsupportedEncodingException e) {
            headers.trace("Api#UnsupportedEncodingException(2): " + e);
            Logger.log.error("Api url", e);
            throw new ApiException("encoding");
        } catch (IOException e) {
            headers.trace("Api#IOException(2): " + e);
            Logger.log.error("Api url", e);
            throw new ApiException("io");
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    Logger.log.error("Api close", e);
                }
            }
        }
    }

    private ByteArrayOutputStream gzipStream(byte[] input) throws IOException, UnsupportedEncodingException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        GZIPOutputStream gz = new GZIPOutputStream(buffer);
        try {
            gz.write(input);
        } finally {
            gz.close();
        }
        return buffer;
    }

    private String extractHtml(InputStream input) throws ApiException, IOException, UnsupportedEncodingException {
        headers.trace("Api#extractHtml");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[READ_BUFFER_SIZE];
        int len = 0;
        while ((len = input.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
        input.close();

        String json = out.toString(responseEncoding);
        LinkedHashMap<String, String> dict = JSON.decode(json);
        String html = dict.get("body");
        if (html == null) {
            headers.trace("Unknown json format " + json);
            Logger.log.error("Unknown json format " + json);
            throw new ApiException("unknown_json_format");
        }
        return html;
    }

    private String getApiBody(String lang, String body) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        appendKeyValue(sb, "url=", headers.getUrlWithoutLanguageCode());
        appendKeyValue(sb, "&token=", settings.projectToken);
        appendKeyValue(sb, "&lang_code=", lang);
        appendKeyValue(sb, "&url_pattern=", settings.urlPattern);
        appendKeyValue(sb, "&body=", body);
        return sb.toString();
    }

    private URL getApiUrl(String lang, String body) throws UnsupportedEncodingException, NoSuchAlgorithmException, MalformedURLException {
        StringBuilder sb = new StringBuilder();
        sb.append(settings.apiUrl);
        sb.append("translation?cache_key=");
        appendValue(sb, "(token=");
        appendValue(sb, settings.projectToken);
        appendValue(sb, "&settings_hash=");
        appendValue(sb, settings.hash());
        appendValue(sb, "&body_hash=");
        appendValue(sb, hash(body.getBytes()));
        appendValue(sb, "&path=");
        appendValue(sb, headers.pathName);
        appendValue(sb, "&lang=");
        appendValue(sb, lang);
        appendValue(sb, "&version=");
        appendValue(sb, settings.version);
        appendValue(sb, ")");
        return new URL(sb.toString());
    }

    private String hash(byte[] item) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(item);
        byte[] digest = md.digest();
        return DatatypeConverter.printHexBinary(digest).toUpperCase();
    }

    private void appendKeyValue(StringBuilder sb, String key, String value) throws UnsupportedEncodingException {
        sb.append(key);
        appendValue(sb, value);
    }

    private void appendValue(StringBuilder sb, String value) throws UnsupportedEncodingException {
        sb.append(URLEncoder.encode(value, "UTF-8"));
    }
}
