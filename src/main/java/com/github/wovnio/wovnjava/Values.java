package com.github.wovnio.wovnjava;

import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.arnx.jsonic.JSON;

class Values {
    private LinkedHashMap<String,LinkedHashMap<String,LinkedHashMap<String,ArrayList<LinkedHashMap<String,String>>>>>
        values;

    Values(String json) {
        LinkedHashMap<String,LinkedHashMap<String,LinkedHashMap<String,ArrayList<LinkedHashMap<String,String>>>>> v
                = JSON.decode(json);
        this.values = removeJsessionid(v);
    }

    private static LinkedHashMap<String,LinkedHashMap<String,LinkedHashMap<String,ArrayList<LinkedHashMap<String,String>>>>>
        removeJsessionid(LinkedHashMap<String,LinkedHashMap<String,LinkedHashMap<String,ArrayList<LinkedHashMap<String,String>>>>> v)
    {
        if (!v.containsKey("img_vals")) {
            return v;
        }

        LinkedHashMap<String,LinkedHashMap<String,ArrayList<LinkedHashMap<String,String>>>> newImgVals
                = new LinkedHashMap<String,LinkedHashMap<String,ArrayList<LinkedHashMap<String,String>>>>();

        for (Map.Entry<String, LinkedHashMap<String, ArrayList<LinkedHashMap<String, String>>>> map : v.get("img_vals").entrySet()) {
            String key = map.getKey();
            Matcher m = Pattern.compile(";jsessionid=[^=]+$", Pattern.CASE_INSENSITIVE).matcher(key);
            if (!m.find()) {
                newImgVals.put(key, map.getValue());
            } else {
                String newKey = m.replaceFirst("");
                newImgVals.put(newKey, map.getValue());
            }
        }

        v.put("img_vals", newImgVals);
        return v;
    }

    ArrayList<String> getLangs() {
        Set<String> set = new HashSet<String>();

        // Get languages from text data.
        LinkedHashMap<String,LinkedHashMap<String,ArrayList<LinkedHashMap<String,String>>>> textVals
            = this.values.get("text_vals");

        if (textVals != null) {
            for (LinkedHashMap<String, ArrayList<LinkedHashMap<String,String>>> v : textVals.values()) {
                set.addAll(v.keySet());
            }
        }

        // Get languages form image data.
        LinkedHashMap<String,LinkedHashMap<String,ArrayList<LinkedHashMap<String,String>>>> imgVals
            = this.values.get("img_vals");

        if (imgVals != null) {
            for (LinkedHashMap<String, ArrayList<LinkedHashMap<String,String>>> v : imgVals.values()) {
                set.addAll(v.keySet());
            }
        }

        // Remove blank.
        set.remove("");

        // Change variable type and sort.
        ArrayList<String> langs = new ArrayList<String>();
        langs.addAll(set);
        Collections.sort(langs);

        return langs;
    }

    @Nullable
    private String getCommon(String type, String text, String lang) {
        if (this.values.get(type) == null) {
            return null;
        }
        if (this.values.get(type).containsKey(text)) {
            if (this.values.get(type).get(text).containsKey(lang)) {
                if (this.values.get(type).get(text).get(lang).size() > 0) {
                    return this.values.get(type).get(text).get(lang).get(0).get("data");
                }
            }
        }
        return null;
    }

    String getText(String text, String lang) {
        return this.getCommon("text_vals", text, lang);
    }

    String getImage(String src, String lang) {
        return this.getCommon("img_vals", src, lang);
    }
}
