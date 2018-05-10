package com.github.wovnio.wovnjava;

import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.net.MalformedURLException;
import java.util.Formatter;
import java.util.zip.GZIPInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;


class API {
    private final String version;
    private final Settings settings;
    private final Headers headers;
    private final String responseEncoding = "UTF-8"; // always API response is UTF8

    API(String version, Headers headers, Settings settings) {
        this.version = version;
        this.headers = headers;
        this.settings = settings;
    }

    String translate(String lang, String body) throws APIException {
        HttpURLConnection con = null;
        try {
            URL url = getApiUrl(lang, body);
            String post = getApiBody(lang, body);

            con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Accept-Encoding", "gzip");
            con.setDoOutput(true);
            con.setRequestMethod("POST");

            DataOutputStream data = new DataOutputStream(con.getOutputStream());
            data.writeBytes(post);
            data.close();

            con.connect();
            int status = con.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                InputStream input = null;
                if ("gzip".equals(con.getContentEncoding())) {
                    input = new GZIPInputStream(con.getInputStream());
                }
                else {
                    input = con.getInputStream();
                }
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buffer = new byte[8 * 1024];
                int len = 0;
                while ((len = input.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
                input.close();
                return out.toString(responseEncoding);
            } else {
                throw new APIException("status_" + String.valueOf(status));
            }
        } catch (Exception e) {
            // logging original exception
            throw new APIException("unknown");
        } finally {
            con.disconnect();
        }
    }

    private String getApiBody(String lang, String body) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
		appendField(sb, "url", headers.url, ",");
		appendField(sb, "token", settings.projectToken, ",");
		appendField(sb, "lang_code", lang, ",");
		appendField(sb, "url_pattern", settings.urlPattern, ",");
		appendField(sb, "body", body, "}");
        return sb.toString();
    }

	private void appendField(StringBuilder sb, String key, String value, String suffix) {
        appendString(key);
		sb.append(':');
        appendString(value);
        sb.append(suffix);
    }

	private void appendString(String value) {
		// @see http://www.ietf.org/rfc/rfc4627.txt
		StringBuilder sb = new StringBuilder(value.length() + 4);
		Formatter formatter = new Formatter(sb);
		sb.append('"');
		for (int i = 0; i < value.length(); i += 1) {
			char c = value.charAt(i);
			switch (c) {
				case '\\':
				case '"':
					sb.append('\\');
					sb.append(c);
					break;
				case '/':
					sb.append('\\');
					sb.append(c);
					break;
				case '\b':
					sb.append("\\b");
					break;
				case '\f':
					sb.append("\\f");
					break;
				case '\n':
					sb.append("\\n");
					break;
				case '\r':
					sb.append("\\r");
					break;
				case '\t':
					sb.append("\\t");
					break;
				default:
					if (c < ' ') {
						formatter.format("\\u%04x", Integer.toHexString(c));
					} else {
						sb.append(c);
					}
			}
		}
		sb.append('"');
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
