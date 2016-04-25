package com.github.wovnio.wovnjava;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;

import javax.servlet.FilterConfig;

class Store {
    Settings settings;

    Store(FilterConfig config) {
        super();
        settings = new Settings(config);
    }

    Values getValues(String url) {
        url = url.replaceAll("/$", "");

        String json = "";
        try {
            URL uri = new URL(settings.apiUrl + "?token=" + settings.userToken + "&url=" + url);
            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) uri.openConnection();
                connection.setRequestMethod("GET");

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader isr = new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8"));
                    BufferedReader reader = new BufferedReader(isr);
                    try {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            json += line;
                        }
                    } finally {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            WovnServletFilter.log.error("IOException in reader.close()", e);
                        }
                        try {
                            isr.close();
                        } catch (IOException e) {
                            WovnServletFilter.log.error("IOException in isr.close()", e);
                        }
                    }
                }
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        } catch (IOException e) {
            WovnServletFilter.log.error("IOException in getValues()", e);
        }

        if (json.length() == 0) {
            json = "{}";
        }

        return new Values(json);
    }
}
