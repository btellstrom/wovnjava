package com.github.wovnio.wovnjava;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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

            if (Logger.isDebug()) {
                Logger.log.info("Sending API request to \"" + uri.toString() + "\"");
            }

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
                    } catch (IOException e) {
                        Logger.log.error("IOException in reader.readLine()", e);
                    } finally {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            Logger.log.error("IOException in reader.close()", e);
                        }
                        try {
                            isr.close();
                        } catch (IOException e) {
                            Logger.log.error("IOException in isr.close()", e);
                        }
                    }
                }
            } catch (IOException e) {
                Logger.log.error("IOException in uri.openConnection()", e);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        } catch (MalformedURLException e) {
            Logger.log.error("MalformedURLException in parsing URL", e);
        }

        if (Logger.isDebug()) {
            if (Logger.debugMode > 1) {
                Logger.log.info("Translation data: " + json);
            } else {
                if (json.isEmpty()) {
                    Logger.log.info("Translation data is empty string");
                } else if (json.equals("{}")) {
                    Logger.log.info("Translation data is empty");
                } else {
                    Logger.log.info("Translation data size: " + json.length());
                }
            }
        }

        if (json.isEmpty()) {
            json = "{}";
        }

        return new Values(json);
    }
}
