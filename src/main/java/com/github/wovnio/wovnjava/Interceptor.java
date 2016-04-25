package com.github.wovnio.wovnjava;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

import us.codecraft.xsoup.Xsoup;

class Interceptor {
    private Store store;

    Interceptor(FilterConfig config) {
        store = new Store(config);
    }

    void call(HttpServletRequest request, ServletResponse response, FilterChain chain) {

        if (!store.settings.isValid()) {
            try {
                chain.doFilter(request, response);
            } catch (ServletException e) {
            } catch (IOException e) {
            }
            return;
        }

        Headers h = new Headers(request, store.settings);
        if (store.settings.testMode && !store.settings.testUrl.equals(h.url)) {
            try {
                chain.doFilter(request, response);
            } catch (ServletException e) {
            } catch (IOException e) {
            }
        }

        if (h.getPathLang().equals(store.settings.defaultLang)) {
            try {
                ((HttpServletResponse) response).sendRedirect(
                        h.redirectLocation(store.settings.defaultLang)
                );
            } catch (IOException e) {
            }
            return;
        }

        WovnHttpServletRequest wovnRequest = new WovnHttpServletRequest(
                request, h
        );

        WovnHttpServletResponse wovnResponse = new WovnHttpServletResponse(
                (HttpServletResponse)response, h
        );

        try {
            chain.doFilter(wovnRequest, wovnResponse);
        } catch (ServletException e) {
        } catch (IOException e) {
        }

        String body = null;
        // There is a possibility that response.getContentType() is null when the response is an image.
        if (response.getContentType() != null && Pattern.compile("html").matcher(response.getContentType()).find()) {
            Values values = store.getValues(h.pageUrl);
            if (!String.valueOf(wovnResponse.status).matches("^1|302")) {
                String lang = h.langCode();
                HashMap<String,String> url = new HashMap<String,String>();
                url.put("protocol", h.protocol);
                url.put("host", h.host);
                url.put("pathname", h.pathName);

                body = wovnResponse.toString();
                body = this.switchLang(body, values, url, lang, h);
                wovnResponse.setContentLength(body.getBytes().length);
            }
        }

        if (body != null) {
            // text
            try {
                PrintWriter out = response.getWriter();
                out.write(body);
            } catch (IOException e) {
            }
        } else {
            // binary
            try {
                ServletOutputStream out = response.getOutputStream();
                out.write(wovnResponse.getData());
                out.close();
            } catch (IOException e) {
            }
        }

        h.out(request, wovnResponse);
    }

    private String addLangCode(String href, String pattern, String lang, Headers headers) {
        if (href != null && href.length() > 0 && href.matches("^(#.*)?$")) {
            return href;
        }

        String newHref = href;

        if ( href != null
                && href.length() > 0
                && Pattern.compile("^(https?:)?//", Pattern.CASE_INSENSITIVE).matcher(href).find()
                ) {

            URL uri;
            try {
                uri = new URL(href);
            } catch (MalformedURLException e) {
                return newHref;
            }
            if (uri.getHost().toLowerCase().equals(headers.host.toLowerCase())) {
                if (pattern.equals("subdomain")) {
                    Matcher m = Pattern.compile("//([^\\.]*)\\.").matcher(href);
                    String subCode = Lang.getCode(m.group(1));
                    if (subCode != null
                            && subCode.length() > 0
                            && subCode.toLowerCase().equals(lang.toLowerCase())
                            ) {
                        newHref = Pattern.compile(lang, Pattern.CASE_INSENSITIVE)
                                .matcher(href)
                                .replaceFirst(lang.toLowerCase());
                    } else {
                        newHref = href.replaceFirst("(//)([^\\.]*)", "$1" + lang.toLowerCase() + ".$2");
                    }
                } else if (pattern.equals("query")) {
                    if (href.matches("\\?")) {
                        newHref = href + "&wovn=" + lang;
                    } else {
                        newHref = href + "?wovn=" + lang;
                    }
                } else {
                    newHref = href.replaceFirst("([^\\.]*\\.[^/]*)(/|$)", "$1/" + lang + "/");
                }
            }
        } else if (href != null && href.length() > 0) {
            String currentDir;
            if (pattern.equals("subdomain")) {
                String langUrl = headers.protocol + "://" + lang.toLowerCase() + headers.host;
                currentDir = headers.pathName.replaceFirst("[^/]*\\.[^\\.]{2,6}$", "");
                if (href.matches("^\\.\\..*$")) {
                    newHref = langUrl + "/" + href.replaceAll("^\\.\\./", "");
                } else if (href.matches("^\\..*$")) {
                    newHref = langUrl + currentDir + "/" + href.replaceAll("^\\./", "");
                } else if (href.matches("^/.*$")) {
                    newHref = langUrl + href;
                } else {
                    newHref = langUrl + currentDir + "/" + href;
                }
            } else if (pattern.equals("query")) {
                if (href.matches("\\?")) {
                    newHref = href + "&wovn=" + lang;
                } else {
                    newHref = href + "?wovn=" + lang;
                }
            } else {
                if (href.matches("^/")) {
                    newHref = "/" + lang + href;
                } else {
                    currentDir = headers.pathName.replaceFirst("[^/]*\\.[^\\.]{2,6}$", "");
                    newHref = "/" + lang + currentDir + href;
                }
            }
        }

        return newHref;
    }

    private boolean checkWovnIgnore(Element el) {
        if (el.hasAttr("wovn-ignore")) {
            return true;
        } else if (el.parent() == null) {
            return false;
        }
        return this.checkWovnIgnore(el.parent());
    }

    private String switchLang(String body, Values values, HashMap<String, String> url, String lang, Headers headers) {

        lang = Lang.getCode(lang);
        Document doc = Jsoup.parse(body);

        if (Xsoup.compile("//html[@wovn-ignore]").evaluate(doc).get() != null) {
            return doc.html();
        }

        if (!lang.equals(this.store.settings.defaultLang)) {
            for (Element el : Xsoup.compile("//a").evaluate(doc).getElements()) {
                if (this.checkWovnIgnore(el)) {
                    continue;
                }
                String href = el.attr("href");
                String newHref = this.addLangCode(href, this.store.settings.urlPattern, lang, headers);
                el.attr("href", newHref);
            }
        }

        for (Element el : Xsoup.compile("//text()").evaluate(doc).getElements()) {
            if (this.checkWovnIgnore(el)) {
                continue;
            }
            String nodeText = el.ownText();
            nodeText = Pattern.compile("^\\s+|\\s+$").matcher(nodeText).replaceAll("");
            String destText = values.getText(nodeText, lang);
            if (destText != null) {
                String newText = Pattern.compile("^(\\s*)[\\S\\s]*(\\s*)$")
                        .matcher(el.text())
                        .replaceAll("$1" + destText + "$2");
                el.text(newText);
            }
        }

        Pattern p = Pattern.compile("^(description|title|og:title|og:description|twitter:title|twitter:description)$");
        for (Element el : Xsoup.compile("//meta").evaluate(doc).getElements()) {
            if (this.checkWovnIgnore(el)) {
                continue;
            }
            if ( !(el.hasAttr("name") && p.matcher(el.attr("name")).find())
                    && !(el.hasAttr("property") && p.matcher(el.attr("name")).find())
                    ) {
                continue;
            }
            String nodeContent = el.attr("content");
            if (nodeContent == null) {
                continue;
            }
            nodeContent = Pattern.compile("^\\s+|\\s+$").matcher(nodeContent).replaceAll("");
            if (nodeContent.length() == 0) {
                continue;
            }
            String destContent = values.getText(nodeContent, lang);
            if (destContent != null) {
                String newContent = Pattern.compile("^(\\s*)[\\S\\s]*(\\s*)$")
                        .matcher(nodeContent)
                        .replaceAll("$1" + destContent + "$2");
                el.attr("content", newContent);
            }
        }

        for (Element el : Xsoup.compile("//img").evaluate(doc).getElements()) {
            if (this.checkWovnIgnore(el)) {
                continue;
            }
            Matcher m = Pattern.compile("src=['\"]([^'\"]*)['\"]", Pattern.CASE_INSENSITIVE)
                    .matcher(el.outerHtml());
            if (m.find()) {
                String src = m.group(1);
                if (!Pattern.compile("://").matcher(src).find()) {
                    if (Pattern.compile("^/").matcher(src).find()) {
                        src = url.get("protocol") + "://" + url.get("host") + src;
                    } else {
                        src = url.get("protocol") + "://" + url.get("host") + url.get("path") + src;
                    }
                }
                String destSrc = values.getImage(src, lang);
                if (destSrc != null) {
                    el.attr("src", destSrc);
                }
            }
            if (el.hasAttr("alt")) {
                String alt = el.attr("alt");
                alt = Pattern.compile("^\\s+|\\s+$").matcher(alt).replaceAll("");
                String destAlt = values.getText(alt, lang);
                if (destAlt != null) {
                    String newAlt = Pattern.compile("^(\\s*)[\\S\\s]*(\\s*)$")
                            .matcher(alt)
                            .replaceAll("$1" + destAlt + "$2");
                    el.attr("alt", newAlt);
                }
            }
        }

        for (Element el : Xsoup.compile("//script").evaluate(doc).getElements()) {
            if ( el.hasAttr("src")
                    && Pattern.compile("//j\\.(dev-)?wovn\\.io(:3000)?/").matcher(el.attr("src")).find()
                    ) {
                el.remove();
            }
        }

        Element parentNode = doc.head();
        if (parentNode == null) {
            parentNode = doc.body();
        }
        if (parentNode == null) {
            parentNode = doc;
        }

        Element insertNode = new Element(Tag.valueOf("script"), "");
        insertNode.attr("src", "//j.wovn.io/1");
        insertNode.attr("async", true);
        String version = WovnServletFilter.VERSION;
        insertNode.attr(
                "data-wovnio",
                "key=" + this.store.settings.userToken + "&backend=true&currentLang=" + lang
                        + "&defaultLang=" + this.store.settings.defaultLang
                        + "&urlPattern=" + this.store.settings.urlPattern + "&version=" + version
        );
        insertNode.text(" ");
        parentNode.prependChild(insertNode);

        for (String l : values.getLangs()) {
            insertNode = new Element(Tag.valueOf("link"), "");
            insertNode.attr("ref", "altername");
            insertNode.attr("hreflang", l);
            insertNode.attr("href", headers.redirectLocation(l));
            parentNode.appendChild(insertNode);
        }

        Xsoup.compile("//html").evaluate(doc).getElements().first().attr("lang", lang);

        return doc.html();
    }
}
