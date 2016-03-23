package io.wovn.wovnjava;

import java.util.*;

import javax.servlet.*;

public class Settings {
    public String userToken;
    public String secretKey;
    public String urlPattern = "path";
    public String urlPatternReg = "/(?<lang>[^/.?]+)";
    public ArrayList<String> query;
    public String apiUrl = "https://api.wovn.io/v0/values";
    public String defaultLang = "en";
    public ArrayList<String> supportedLangs;
    public boolean testMode;
    public String testUrl;

    public Settings(FilterConfig config) {
        super();

        this.query = new ArrayList<String>();
        this.supportedLangs = new ArrayList<String>();
        this.supportedLangs.add("en");

        String p;

        p = config.getInitParameter("userToken");
        if (p != null && p.length() > 0) {
            userToken = p;
        }

        p = config.getInitParameter("secretKey");
        if (p != null && p.length() > 0) {
            secretKey = p;
        }

        p = config.getInitParameter("urlPattern");
        if (p != null && p.length() > 0) {
            urlPattern = p;
        }

        p = config.getInitParameter("urlPatternReg");
        if (p != null && p.length() > 0) {
            urlPatternReg = p;
        }

        p = config.getInitParameter("query");
        if (p != null && p.length() > 0) {
            query = getArrayParameter(p);
        }

        p = config.getInitParameter("apiUrl");
        if (p != null && p.length() > 0) {
            apiUrl = p;
        }

        p = config.getInitParameter("defaultLang");
        if (p != null && p.length() > 0) {
            defaultLang = p;
        }

        p = config.getInitParameter("supportedLangs");
        if (p != null && p.length() > 0) {
            supportedLangs = getArrayParameter(p);
        }

        p = config.getInitParameter("testMode");
        if (p != null && p.length() > 0) {
            testMode = getBoolParameter(p);
        }

        p = config.getInitParameter("testUrl");
        if (p != null && p.length() > 0) {
            testUrl = p;
        }

        initialize();
    }

    private static ArrayList<String> getArrayParameter(String param) {
        if (param == null || param.length() == 0) {
            return null;
        }

        param = param.replaceAll("^\\s+(.+)\\s+$", "$1");
        String[] params = param.split("\\s*,\\s*");
        ArrayList<String> al = new ArrayList<String>();
        for (String p : params) {
            al.add(p);
        }
        return al;
    }

    private static boolean getBoolParameter(String param) {
        param = param.toLowerCase();
        if (param.equals("on") || param.equals("true") || param.equals("1")) {
            return true;
        } else {
            return false;
        }
    }

    private void initialize() {
        defaultLang = Lang.getCode(defaultLang);

        if (supportedLangs.size() == 0) {
            supportedLangs.add(defaultLang);
        }

        if (this.urlPattern.equals("path")) {
            this.urlPatternReg = "/(?<lang>[^/.?]+)";
        } else if (this.urlPattern.equals("query")) {
            this.urlPatternReg = "((\\?.*&)|\\?)wovn=(?<lang>[^&]+)(&|$)";
        } else if (this.urlPattern.equals("subdomain")) {
            this.urlPatternReg = "^(?<lang>[^.]+)\\.";
        }
    }

    public boolean isValid() {
        boolean valid = true;
        ArrayList<String> errors = new ArrayList<String>();

        if (userToken == null || userToken.length() < 5 || userToken.length() > 6) {
            valid = false;
            errors.add("User token is not " + userToken + " valid.");
        }
        if (secretKey == null || secretKey.length() == 0) {
            valid = false;
            errors.add("Secret key is not configured.");
        }
        if (urlPattern == null || urlPattern.length() == 0) {
            valid = false;
            errors.add("Url pattern is not configured.");
        }
        if (apiUrl == null || apiUrl.length() == 0) {
            valid = false;
            errors.add("API URL is not configured.");
        }
        if (defaultLang == null || defaultLang.length() == 0) {
            valid = false;
            errors.add("Default lang is not configured.");
        }
        if (supportedLangs.size() < 1) {
            valid = false;
            errors.add("Supported langs is not configured.");
        }

        if (errors.size() > 0) {
            // save error logs.
        }

        return valid;
    }
}
