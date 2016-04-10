package com.github.wovnio.wovnjava;

import junit.framework.TestCase;

import java.util.List;

public class ValuesTest extends TestCase {

    private static final String JSON
        = "{"
            + "\"text_vals\":{"
                + "\"Orders\":{"
                    + "\"ja\":["
                        + "{"
                            + "\"xpath\":\"/html/body/div/div/ul/li[4]/ul/li[5]/a/text()\","
                            + "\"data\":\"オーダー\""
                        + "}"
                    + "]"
                + "},"
                + "\"Copyright\":{"
                    + "\"\":["
                        + "{"
                            + "\"xpath\":\"\","
                            + "\"data\":\"\""
                        + "}"
                    + "]"
                + "},"
                + "\"Blank\":{"
                    + "\"ja\":["
                        + "{"
                            + "\"xpath\":\"\","
                            + "\"data\":\"\""
                        + "}"
                    + "]"
                + "}"
            + "},"
            + "\"img_vals\":{"
                + "\"https://wovn.io/assets/header_logo.png\":{"
                    + "\"en\":["
                        + "{"
                            + "\"xpath\":\"/html/body/div/a/img\","
                            + "\"data\":\"https://s3-us-west-1.amazonaws.com/st.wovn.io/logo_en.png\""
                        + "}"
                    + "]"
                + "}"
            + "},"
            + "\"layout_vals\":{"
            + "}"
        + "}";

    public void testValues() {
        Values v = new Values(JSON);
        assertNotNull(v);
    }

    public void testGetLangs() {
        Values values = new Values(JSON);
        List<String> langs = values.getLangs();
        assertEquals(2, langs.size());
        assertEquals("en", langs.get(0));
        assertEquals("ja", langs.get(1));
    }

    public void testGetTextExists() {
        Values values = new Values(JSON);
        String text = values.getText("Orders", "ja");
        assertEquals("オーダー", text);
    }

    public void testGetTextNotExists() {
        Values values = new Values(JSON);
        String text = values.getText("Non existing text", "ja");
        assertEquals(null, text);
    }

    public void testGetTextBlank() {
        Values values = new Values(JSON);
        String text = values.getText("Blank", "ja");
        assertEquals("", text);
    }

    public void testGetImage() {
        Values values = new Values(JSON);
        String image = values.getImage("https://wovn.io/assets/header_logo.png", "en");
        assertEquals("https://s3-us-west-1.amazonaws.com/st.wovn.io/logo_en.png", image);
    }
}