package com.github.wovnio.wovnjava;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

class DoctypeDetectation {
    private static Pattern pattern = Pattern.compile("<!doctype\\s+html[^>]*>", Pattern.CASE_INSENSITIVE);

    public static String addDoctypeIfNotExists(String input, String output)
    {
        if (match(output).matches()) {
            return output;
        }

        Matcher m = match(input);
        if (!m.find()) {
            return output;
        }

        String doctype = m.group(0);
        if (doctype.toLowerCase().contains("xhtml")) {
            return doctype + output;
        } else {
            return doctype + output.replaceFirst(" xmlns=\"http://www.w3.org/1999/xhtml\"", "");
        }
    }

    private static Matcher match(String s) {
        return pattern.matcher(normalize(s));
    }

    private static String normalize(String s) {
        return s.substring(0, Math.min(s.length(), bufferSize));
    }

    private static int bufferSize = 256;
}
