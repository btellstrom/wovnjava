package com.github.wovnio.wovnjava;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class Headers {
    Settings settings;
    private HttpServletRequest request;

    private String browserLang;
    String host;
    private String pathLang;
    String pathName;
    String protocol;
    String pageUrl;
    private String query;
    private String unmaskedHost;
    private String unmaskedPathName;
    private String unmaskedUrl;
    String url;

    Headers(HttpServletRequest r, Settings s) {
        this.settings = s;
        this.request = r;

        this.protocol = this.request.getScheme();
        this.unmaskedHost = this.request.getRemoteHost();

        String requestUri = this.request.getRequestURI();
        if (requestUri == null || requestUri.length() == 0) {
            if (Pattern.compile("^[^/]").matcher(this.request.getPathInfo()).find()) {
                requestUri = "/";
            } else {
                requestUri = "";
            }
            requestUri += this.request.getPathInfo();
            if (r.getQueryString() != null && this.request.getQueryString().length() > 0) {
                requestUri += "?" + this.request.getQueryString();
            }
        }
        if (Pattern.compile("://").matcher(requestUri).find()) {
            requestUri = Pattern.compile("^.*://[^/]+").matcher(requestUri).replaceFirst("");
        }
        String[] split = requestUri.split("\\?");
        this.unmaskedPathName = split[0];
        if ( !Pattern.compile("/$").matcher(this.unmaskedPathName).find()
                && !Pattern.compile("/[^/.]+\\.[^/.]+$").matcher(this.unmaskedPathName).find()
                ) {
            this.unmaskedPathName += "/";
        }
        this.unmaskedUrl = this.protocol + "://" + this.unmaskedHost + this.unmaskedPathName;
        if (this.settings.urlPattern.equals("subdomain")) {
            this.host = this.removeLang(this.request.getServerName(), this.langCode());
        } else {
            this.host = r.getServerName();
        }
        this.pathName = split[0];
        if (split.length == 2) {
            this.query = split[1];
        }
        if (this.settings.urlPattern.equals("path")) {
            this.pathName = this.removeLang(this.pathName, this.langCode());
        }
        if (this.query == null || this.query.length() == 0) {
            this.query = "";
        }
        this.url = this.host;
        if (this.request.getServerPort() != 80 && this.request.getServerPort() != 443) {
            this.url += ":" + this.request.getServerPort();
        }
        this.url += this.pathName;
        if (this.query != null && this.query.length() > 0) {
            this.url += "?";
        }
        this.url += this.removeLang(this.query, this.langCode());
        if (this.settings.query.size() > 0) {
            ArrayList<String> queryVals = new ArrayList<String>();
            for (String q : queryVals) {
                Pattern p = Pattern.compile("(^|&)(?<query_val>" + q + "[^&]+)(&|$)");
                Matcher m = p.matcher(this.query);
                if (m.find() && m.group("query_val") != null && m.group("query_val").length() > 0) {
                    queryVals.add(m.group("query_val"));
                }
            }
            if (queryVals.size() > 0) {
                this.query = "?";
                Collections.sort(queryVals);
                for (String q : queryVals) {
                    this.query += q + "&";
                }
                this.query = Pattern.compile("&$").matcher(this.query).replaceFirst("");
            }
        } else {
            this.query = "";
        }
        this.query = this.removeLang(this.query, this.langCode());
        this.pathName = Pattern.compile("/$").matcher(this.pathName).replaceAll("");
        this.pageUrl = this.host + this.pathName + this.query;
    }

    String langCode() {
        String pl = getPathLang();
        if (pl != null && pl.length() > 0) {
            return pl;
        } else {
            return settings.defaultLang;
        }
    }

    String getPathLang() {
        if (this.pathLang == null || this.pathLang.length() == 0) {
            Pattern p = Pattern.compile(settings.urlPatternReg);
            String path = this.request.getServerName() + this.request.getRequestURI();
            if (this.request.getQueryString() != null && this.request.getQueryString().length() > 0) {
                path += "?" + this.request.getQueryString();
            }
            Matcher m = p.matcher(path);
            if (m.find()) {
                String l = m.group("lang");
                if (l != null && l.length() > 0 && Lang.getLang(l) != null) {
                    String lc = Lang.getCode(l);
                    if (lc != null && lc.length() > 0) {
                        this.pathLang = lc;
                        return this.pathLang;
                    }
                }
            }
            this.pathLang = "";
        }
        return this.pathLang;
    }

    // This method is not used in wovnrb.
    /*
    public String getBrowserLang() {
        if (browserLang == null || browserLang.length() == 0) {
            Cookie cookies[] = request.getCookies();
            boolean noCookie = true;
            if (cookies != null) {
                for (Cookie c : cookies) {
                    String name = c.getName();
                    if (name.equals("wovn_selected_lang")) {
                        String value = c.getValue();
                        if (Lang.getLang(value) != null) {
                            browserLang = value;
                            noCookie = false;
                        }
                        break;
                    }
                }
            }
            if (noCookie) {
                browserLang = "";
                Enumeration e = request.getHeaders("Accept-Language");
                while (e.hasMoreElements()) {
                    String l = (String) e.nextElement();
                    if (Lang.getLang(l) != null) {
                        browserLang = l;
                        break;
                    }
                }
            }
        }
        return browserLang;
    }
    */

    String redirectLocation(String lang) {
        if (lang.equals(this.settings.defaultLang)) {
            return this.protocol + "://" + this.url;
        } else {
            String location = this.url;
            if (this.settings.urlPattern.equals("query")) {
                if (!location.matches("\\?")) {
                    location = location + "?wovn=" + lang;
                } else if (!this.request.getRequestURI().matches("(\\?|&)wovn=")) {
                    location = location + "&wovn=" + lang;
                }
            } else if (this.settings.urlPattern.equals("subdomain")) {
                location = lang.toLowerCase() + "." + location;
            } else {
                location = location.replaceFirst("(/|$)", lang);
            }
            return protocol + "://" + location;
        }
    }

    String removeLang(String uri, String lang) {
        if (lang == null || lang.length() == 0) {
            lang = this.getPathLang();
        }
        if (this.settings.urlPattern.equals("query")) {
            return uri.replaceFirst("(^|\\?|&)wovn=" + lang + "(&|$)", "$1")
                    .replaceAll("(\\?|&)$", "");
        } else if (this.settings.urlPattern.equals("subdomain")) {
            return Pattern.compile("(^|(//))" + lang + "\\.", Pattern.CASE_INSENSITIVE)
                    .matcher(uri).replaceFirst("$1");
        } else {
            return uri.replaceFirst("/" + lang + "(/|$)", "/");
        }
    }

    void out(HttpServletRequest req, HttpServletResponse res) {
        String loc = req.getHeader("Location");
        if (loc != null && loc.matches("//" + host)) {
            if (this.settings.urlPattern.equals("query")) {
                if (loc.matches("\\?")) {
                    res.setHeader("Location", loc + "?wovn=" + langCode());
                } else {
                    res.setHeader("Location", loc + "&wovn=" + langCode());
                }
            } else if (this.settings.urlPattern.equals("subdomain")) {
                loc = loc.replaceFirst("//([^.]+)", "//" + langCode() + "\\.$1");
                res.setHeader("Location", loc);
            } else {
                loc = loc.replaceFirst("(//[^/]+)", "$1/" + langCode());
                res.setHeader("Location", loc);
            }
        }
    }
}
