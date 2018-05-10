package com.github.wovnio.wovnjava;

class HtmlConverter {
    private final String original;

    HtmlConverter(String original) {
        this.original = original;
    }

    String strip() {
        return original;
    }

    String restore(String html) {
        return html;
    }
}
