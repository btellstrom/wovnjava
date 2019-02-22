package com.github.wovnio.wovnjava;

class Interceptor {
    private final Settings settings;
    private final Headers headers;
    private final Api api;

    Interceptor(Headers headers, Settings settings, Api api) {
        this.headers = headers;
        this.settings = settings;
        this.api = api;
    }

    String translate(String body) {
        headers.trace("Interceptor#translate");
        String lang = headers.getPathLang();
        boolean canTranslate = lang.length() > 0 && !lang.equals(settings.defaultLang);
        if (canTranslate) {
            return apiTranslate(lang, body);
        } else {
            return localTranslate(settings.defaultLang, body);
        }
    }

    private String apiTranslate(String lang, String body) {
        headers.trace("Interceptor#apiTranslate");
        try {
            HtmlConverter converter = new HtmlConverter(settings, body);
            String convertedBody = converter.strip();
            String translatedBody = api.translate(lang, convertedBody);
            return converter.restore(translatedBody);
        } catch (ApiException e) {
            headers.trace("Interceptor#ApiException " + e);
            Logger.log.error("ApiException", e);
            return apiTranslateFail(body, lang, e.getMessage());
        }
    }

    private String apiTranslateFail(String body, String lang, String reason) {
        headers.trace("Interceptor#apiTranslateFail");
        return new HtmlConverter(settings, body).convert(headers, lang, reason);
    }

    private String localTranslate(String lang, String body) {
        headers.trace("Interceptor#localTranslate");
        return new HtmlConverter(settings, body).convert(headers, lang, "backend_without_api");
    }
}
