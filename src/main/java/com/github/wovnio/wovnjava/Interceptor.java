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
            HtmlConverter converter = new HtmlConverter(settings, body);
		    String convertedBody = converter.strip();
		    String translatedBody = api.translate(lang, convertedBody);
            return converter.restore(translatedBody);
        } catch (ApiException e) {
            Logger.log.error("ApiException", e);
            return apiTranslateFail(body, e.getMessage());
        }
	}

    private String apiTranslateFail(String body, String reason) {
        return new HtmlConverter(settings, body).convert(headers, settings.defaultLang, reason);
    }

    private String localTranslate(String lang, String body) {
        return new HtmlConverter(settings, body).convert(headers, lang, "backend_without_api");
    }
}
