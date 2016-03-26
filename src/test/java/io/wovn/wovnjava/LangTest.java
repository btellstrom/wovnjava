package io.wovn.wovnjava;

import junit.framework.TestCase;

import java.util.Map;

public class LangTest extends TestCase {

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

    public void testLang() {
        Lang lang = new Lang("日本語", "ja", "Japanese");
        assertNotNull(lang);
        assertEquals("日本語", lang.name);
        assertEquals("ja", lang.code);
        assertEquals("Japanese", lang.en);
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

    public void testGetLangWithValidCode() {
        assertSame(Lang.LANG.get("ms"), Lang.getLang("ms"));
    }

    public void testGetLangWithCapitalLetters() {
        assertSame(Lang.LANG.get("zh-CHT"), Lang.getLang("zh-cht"));
    }

    public void testGetLangWithValidEnglishName() {
        assertSame(Lang.LANG.get("pt"), Lang.getLang("Portuguese"));
    }

    public void testGetLangWithValidNativeName() {
        assertSame(Lang.LANG.get("hi"), Lang.getLang("हिन्दी"));
    }

    public void testGetLangWithInvalidName() {
        assertSame(null, Lang.getLang("WOVN4LYFE"));
    }

    public void testGetLangWithEmptyString() {
        assertSame(null, Lang.getLang(""));
    }

    public void testGetLangWithNull() {
        assertSame(null, Lang.getLang(null));
    }

}