package io.wovn.wovnjava;

import junit.framework.TestCase;

import java.util.Map;

public class LangTest extends TestCase {

    public LangTest(String name) {
        super(name);
    }

    public void testLangsExist() {
        assertNotNull(Lang.LANG);
    }

    public void testKeyExist() {
        for (Map.Entry<String, Lang> e : Lang.LANG.entrySet()) {
            String k = e.getKey();
            Lang l = e.getValue();
            assertFalse(l.name == null || l.name.length() == 0);
            assertFalse(l.code == null || l.code.length() == 0);
            assertFalse(l.en == null || l.en.length() == 0);
            assertEquals(k, l.code);
        }
    }

    public void testGetCodeWithValidCode() {
        assertEquals("ms", Lang.getCode("ms"));
    }

    public void testGetCodeWithCapitalLetters() {
        assertEquals("zh-CHT", Lang.getCode("zh-cht"));
    }

    public void testGetCodeWithValidEnglishName() {
        assertEquals("pt", Lang.getCode("Portuguese"));
    }

    public void testGetCodeWithValidNativeName() {
        assertEquals("hi", Lang.getCode("हिन्दी"));
    }

    public void testGetCodeWithInvalidName() {
        assertEquals(null, Lang.getCode("WOVN4LYFE"));
    }

    public void testGetCodeWithEmptyString() {
        assertEquals(null, Lang.getCode(""));
    }

    public void testGetCodeWithNull() {
        assertEquals(null, Lang.getCode(null));
    }

}