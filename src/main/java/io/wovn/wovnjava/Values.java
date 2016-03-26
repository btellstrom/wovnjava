package io.wovn.wovnjava;

import net.arnx.jsonic.JSON;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

class Values {
    private LinkedHashMap<String,LinkedHashMap<String,LinkedHashMap<String,ArrayList<LinkedHashMap<String,String>>>>> values;

    Values(String json) {
        values = JSON.decode(json);
    }

    ArrayList<String> getLangs() {
        Set<String> set = new HashSet<String>();

        // Get languages from text data.
        LinkedHashMap<String,LinkedHashMap<String,ArrayList<LinkedHashMap<String,String>>>> textVals = this.values.get("text_vals");
        if (textVals != null) {
            for (LinkedHashMap<String, ArrayList<LinkedHashMap<String,String>>> v : textVals.values()) {
                set.addAll(v.keySet());
            }
        }

        // Get languages form image data.
        LinkedHashMap<String,LinkedHashMap<String,ArrayList<LinkedHashMap<String,String>>>> imgVals = this.values.get("img_vals");
        if (imgVals != null) {
            for (LinkedHashMap<String, ArrayList<LinkedHashMap<String,String>>> v : imgVals.values()) {
                set.addAll(v.keySet());
            }
        }

        // Remove blank.
        set.remove("");

        // Change variable type.
        ArrayList<String> langs = new ArrayList<String>();
        langs.addAll(set);

        return langs;
    }

    @org.jetbrains.annotations.Nullable
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
