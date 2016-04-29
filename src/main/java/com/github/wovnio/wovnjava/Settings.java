package com.github.wovnio.wovnjava;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.FilterConfig;

class Settings {
    static final String UrlPatternRegPath = "/([^/.?]+)";
    static final String UrlPatternRegQuery = "(?:(?:\\?.*&)|\\?)wovn=([^&]+)(?:&|$)";
    static final String UrlPatternRegSubdomain = "^([^.]+)\\.";

    String userToken = "";
    String secretKey = "";
    String urlPattern = "path";
    String urlPatternReg = UrlPatternRegPath;
    ArrayList<String> query;
    String apiUrl = "https://api.wovn.io/v0/values";
    String defaultLang = "en";
    ArrayList<String> supportedLangs;
    boolean testMode = false;
    String testUrl = "";

    Settings(FilterConfig config) {
        super();

        this.query = new ArrayList<String>();
        this.supportedLangs = new ArrayList<String>();
        this.supportedLangs.add("en");

        String p;

        p = config.getInitParameter("userToken");
        if (p != null && p.length() > 0) {
            this.userToken = p;
        }

        p = config.getInitParameter("secretKey");
        if (p != null && p.length() > 0) {
            this.secretKey = p;
        }

        p = config.getInitParameter("urlPattern");
        if (p != null && p.length() > 0) {
            this.urlPattern = p;
        }

        p = config.getInitParameter("urlPatternReg");
        if (p != null && p.length() > 0) {
            this.urlPatternReg = p;
        }

        p = config.getInitParameter("query");
        if (p != null && p.length() > 0) {
            this.query = getArrayParameter(p);
        }

        p = config.getInitParameter("apiUrl");
        if (p != null && p.length() > 0) {
            this.apiUrl = p;
        }

        p = config.getInitParameter("defaultLang");
        if (p != null && p.length() > 0) {
            this.defaultLang = p;
        }

        p = config.getInitParameter("supportedLangs");
        if (p != null && p.length() > 0) {
            this.supportedLangs = getArrayParameter(p);
        }

        p = config.getInitParameter("testMode");
        if (p != null && p.length() > 0) {
            this.testMode = getBoolParameter(p);
        }

        p = config.getInitParameter("testUrl");
        if (p != null && p.length() > 0) {
            this.testUrl = p;
        }

        this.initialize();
    }

    @Contract("null -> null")
    static ArrayList<String> getArrayParameter(String param) {
        if (param == null || param.length() == 0) {
            return null;
        }

        param = param.replaceAll("^\\s+(.+)\\s+$", "$1");
        String[] params = param.split("\\s*,\\s*");
        ArrayList<String> al = new ArrayList<String>();
        Collections.addAll(al, params);
        return al;
    }

    @Contract("null -> false")
    static boolean getBoolParameter(String param) {
        if (param == null) {
            return false;
        }
        param = param.toLowerCase();
        return param.equals("on") || param.equals("true") || param.equals("1");
    }

    private void initialize() {
        this.defaultLang = Lang.getCode(this.defaultLang);

        if (this.supportedLangs.size() == 0) {
            this.supportedLangs.add(this.defaultLang);
        }

        if (this.urlPattern.equals("path")) {
            this.urlPatternReg = this.UrlPatternRegPath;
        } else if (this.urlPattern.equals("query")) {
            this.urlPatternReg = this.UrlPatternRegQuery;
        } else if (this.urlPattern.equals("subdomain")) {
            this.urlPatternReg = this.UrlPatternRegSubdomain;
        }
    }

    boolean isValid() {
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
            errors.size();  // This is a dummy code. Should log errors here.
        }

        return valid;
    }
}
