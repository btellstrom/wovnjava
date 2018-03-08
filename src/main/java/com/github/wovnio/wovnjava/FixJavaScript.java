package com.github.wovnio.wovnjava;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

class FixJavaScript {

    static final String marker = "<script>/* WOVNJAVA-SCRIPT-TAG-MARKER */</script>";
    static final Pattern scriptPattern = Pattern.compile("<script[\\s>].*?</script>");
    static final Pattern markerPattern = Pattern.compile(Pattern.quote(marker));

    final ArrayList<String> memory = new ArrayList<String>();
    final String escapedHtml;

    public FixJavaScript(String html) {
        Matcher m = scriptPattern.matcher(html);
        StringBuffer sb = new StringBuffer(html.length());
        while (m.find()) {
            memory.add(m.group());
            m.appendReplacement(sb, marker);
        }
        m.appendTail(sb);
        escapedHtml = sb.toString();
    }

    public String escape() {
        return escapedHtml;
    }

    public String unescape(String html) {
        Matcher m = markerPattern.matcher(html);
        StringBuffer sb = new StringBuffer(html.length());
        int index = 0;
        while (m.find()) {
            m.appendReplacement(sb, memory.get(index));
            ++index;
            if (index >= memory.size()) {
                break;
            }
        }
        m.appendTail(sb);
        return sb.toString();
    }
}
