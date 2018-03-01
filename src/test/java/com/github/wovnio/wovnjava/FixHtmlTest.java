package com.github.wovnio.wovnjava;

import junit.framework.TestCase;


public class FixHtmlTest extends TestCase {
	public void testDeleteInvalidClosingTag() {
		String html = "<!DOCTYPE html>"
			+ "<html>"
			+ "<head>"
			+ "<meta content=\"b\" name=\"a\"></meta>"
			+ "</head>"
			+ "<body>"
			+ "</area>"
			+ "</base>"
			+ "</br>"
			+ "</col>"
			+ "</embed>"
			+ "</hr>"
			+ "</img>"
			+ "</input>"
			+ "</keygen>"
			+ "</link>"
			+ "</param>"
			+ "</source>"
			+ "</track>"
			+ "</wbr>"
			+ "<area></area>"
			+ "<base></base>"
			+ "<br></br>"
			+ "<col></col>"
			+ "<embed></embed>"
			+ "<hr></hr>"
			+ "<img></img>"
			+ "<input></input>"
			+ "<keygen></keygen>"
			+ "<link></link>"
			+ "<meta></meta>"
			+ "<param></param>"
			+ "<source></source>"
			+ "<track></track>"
			+ "<wbr></wbr>"
			+ "</body>"
			+ "</html>";
        String result = FixHtml.deleteClosingTagIfNeed(html);
        String expect = "<!DOCTYPE html><html><head><meta content=\"b\" name=\"a\"></head><body><area><base><br><col><embed><hr><img><input><keygen><link><meta><param><source><track><wbr></body></html>";
        assertEquals(expect, result);
    }
}
