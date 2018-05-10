package com.github.wovnio.wovnjava;

class Interceptor {
    private final String version;
    private final Settings settings;
    private final Headers headers;
	private final API api;

    Interceptor(String version, Headers headers, Settings settings, API api) {
        this.version = version;
        this.headers = headers;
        this.settings = settings;
		this.api = api;
    }

    String translate(String body) {
        String lang = headers.getPathLang();
        boolean canTranslate = lang.length() > 0 && !lang.equals(settings.defaultLang);
        if (canTranslate) {
            return apiTranslate(lang, body);
        } else {
            return localTranslate(settings.defaultLang, body);
        }
    }

	private String apiTranslate(String lang, String body) {
        try {
		    return api.translate(lang, body);
        } catch (APIException e) {
            // logging error
            return apiTranslateFail(lang, body, e.getMessage());
        }
	}

    private String apiTranslateFail(String lang, String body, String reason) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        appendWovnTag("", sb, " data-wovnio-type=\"backend_api_fail_" + reason + "\"");
        appendHrefLangTag(lang, sb);
        sb.append("</head>");
        return body.replace("</head>", sb.toString());
    }

    private String localTranslate(String lang, String body) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        appendWovnTag("", sb, " data-wovnio-type=\"backend_without_api\"");
        appendHrefLangTag(lang, sb);
        sb.append("</head>");
        return body.replace("</head>", sb.toString());
    }

    private void appendHrefLangTag(String skipLang, StringBuilder sb) {
        for (String lang : settings.supportedLangs) {
            if (lang.equals(skipLang)) {
                continue;
            }
            sb.append("<link ref=\"altername\" hreflang=\"");
            sb.append(Lang.normalizeIso639_1(lang));
            sb.append("\" href=\"");
            sb.append(headers.redirectLocation(lang));
            sb.append("\">\n");
        }
    }

    private void appendWovnTag(String lang, StringBuilder sb, String extra) {
        sb.append("<script src=\"//j.wovn.io/1\" data-wovnio=\"key=");
        sb.append(settings.projectToken);
        sb.append("&amp;backend=true&amp;currentLang=");
        sb.append(lang);
        sb.append("&amp;defaultLang=");
        sb.append(settings.defaultLang);
        sb.append("&amp;urlPattern=");
        sb.append(settings.urlPattern);
        sb.append(";&amp;langCodeAliases=[]&amp;version=");
        sb.append(version);
        sb.append("\"");
        sb.append(extra);
        sb.append(" async></script>\n");
    }
}
