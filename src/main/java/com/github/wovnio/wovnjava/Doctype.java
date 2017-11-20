package com.github.wovnio.wovnjava;

class DoctypeDetectation {
    public static String addDoctypeIfNotExists(String input, String output)
    {
        if (normalize(output).contains("<!doctype html")) {
            return output;
        }

        String head = normalize(input);
        for (int i=0; i<htmlDocTypes.length; i++) {
            String doctypeLowerCase = htmlDocTypesLowerCase[i];
            if (head.contains(doctypeLowerCase)) {
                String doctype = htmlDocTypes[i];
                return doctype + output.replaceFirst(" xmlns=\"http://www.w3.org/1999/xhtml\"", "");
            }
        }
        for (int i=0; i<xhtmlDocTypes.length; i++) {
            String doctypeLowerCase = xhtmlDocTypesLowerCase[i];
            if (head.contains(doctypeLowerCase)) {
                String doctype = xhtmlDocTypes[i];
                return doctype + output;
            }
        }

        return output;
    }

    private static String normalize(String s) {
        return s.substring(0, Math.min(s.length(), bufferSize)).trim().replaceAll("\\s+", " ").toLowerCase();
    }

    private static String[] htmlDocTypes = {
        "<!DOCTYPE html>",
        "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">",
        "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">",
        "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Frameset//EN\" \"http://www.w3.org/TR/html4/frameset.dtd\">"
    };

    private static String[] htmlDocTypesLowerCase = {
        "<!doctype html>",
        "<!doctype html public \"-//w3c//dtd html 4.01//en\" \"http://www.w3.org/tr/html4/strict.dtd\">",
        "<!doctype html public \"-//w3c//dtd html 4.01 transitional//en\" \"http://www.w3.org/tr/html4/loose.dtd\">",
        "<!doctype html public \"-//w3c//dtd html 4.01 frameset//en\" \"http://www.w3.org/tr/html4/frameset.dtd\">"
    };

    private static String[] xhtmlDocTypes = {
        "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">",
        "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">",
        "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">",
        "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Frameset//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd\">"
    };

    private static String[] xhtmlDocTypesLowerCase = {
        "<!doctype html public \"-//w3c//dtd xhtml 1.1//en\" \"http://www.w3.org/tr/xhtml11/dtd/xhtml11.dtd\">",
        "<!doctype html public \"-//w3c//dtd xhtml 1.0 transitional//en\" \"http://www.w3.org/tr/xhtml1/dtd/xhtml1-transitional.dtd\">",
        "<!doctype html public \"-//w3c//dtd xhtml 1.0 strict//en\" \"http://www.w3.org/tr/xhtml1/dtd/xhtml1-strict.dtd\">",
        "<!doctype html public \"-//w3c//dtd xhtml 1.0 frameset//en\" \"http://www.w3.org/tr/xhtml1/dtd/xhtml1-frameset.dtd\">"
    };

    private static int bufferSize = 256;
}
