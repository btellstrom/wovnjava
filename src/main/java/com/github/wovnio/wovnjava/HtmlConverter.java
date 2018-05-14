package com.github.wovnio.wovnjava;

import java.lang.StringBuilder;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

class HtmlConverter {
    private final ArrayList<String> markers = new ArrayList<String>();
    private final String prefix = "wovn-marker-";
    private final Document doc;
    private final Settings settings;

    HtmlConverter(Settings settings, String original) {
        this.settings = settings;
        doc = Jsoup.parse(original);
    }

    String strip() {
        removeSnipetAndScripts();
        removeHrefLangIfConflicts();
        removeWovnIgnore();
        removeForm();
        return doc.outerHtml();
    }

    String restore(String html) {
        StringBuilder sb = new StringBuilder();
        String[] list = html.split("<!--" + prefix);

        sb.append(list[0]);
        for (int i=1; i<list.length; i++) {
            String fragment = list[i];
            String commentSuffix = "-->";
            int suffixOffset = fragment.indexOf(commentSuffix);
            String indexString = fragment.substring(0, suffixOffset);
            int index = Integer.parseInt(indexString);
            sb.append(markers.get(index));
            sb.append(fragment.substring(suffixOffset + commentSuffix.length()));
        }
        return sb.toString();
    }

    private void removeHrefLangIfConflicts() {
        Elements elements = doc.head().getElementsByTag("link");
        for (Element element : elements) {
            String hreflang = element.attr("hreflang");
            if (hreflang != null && settings.supportedLangs.contains(hreflang.toLowerCase())) {
                element.remove();
            }
        }
    }

    private void removeSnipetAndScripts() {
        Elements elements = doc.getElementsByTag("script");
        for (Element element : elements) {
            String src = element.attr("src");
            if (src != null && (src.startsWith("//j.wovn.io/") || src.startsWith("//j.dev-wovn.io:3000/"))) {
                element.remove();
            } else {
                replaceNodeToMarkerComment(element);
            }
        }
    }

    private void removeWovnIgnore() {
        Elements elements = doc.getElementsByAttribute("wovn-ignore");
        for (Element element : elements) {
            replaceNodeToMarkerComment(element);
        }
    }

    private void removeForm() {
        Elements elements = doc.getElementsByTag("input");
        for (Element element : elements) {
            String type = element.attr("type");
            if (type != null && type.toLowerCase().equals("hidden")) {
                replaceNodeToMarkerComment(element);
            }
        }
    }

    private void replaceNodeToMarkerComment(Element element) {
        Comment comment = new Comment(prefix + String.valueOf(markers.size()));
        element.replaceWith(comment);
        markers.add(restore(element.outerHtml())); // restore original text if element has marker
    }
}
