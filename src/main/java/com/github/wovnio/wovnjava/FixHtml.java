package com.github.wovnio.wovnjava;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

class FixHtml {
    private static Pattern selfClosingPattern = Pattern.compile("</(?:area|base|br|col|embed|hr|img|input|keygen|link|meta|param|source|track|wbr)>", Pattern.CASE_INSENSITIVE);

    public static String deleteClosingTagIfNeed(String html)
    {
        if (html.substring(0, Math.min(html.length(), bufferSize)).contains("-//W3C//DTD XHTML")) {
            return html;
        }
        Matcher m = selfClosingPattern.matcher(html);
        return m.replaceAll("");
    }

    private static int bufferSize = 256;
}
