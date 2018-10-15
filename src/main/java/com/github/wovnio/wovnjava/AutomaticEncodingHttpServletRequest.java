package com.github.wovnio.wovnjava;

import java.util.HashMap;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.nio.CharBuffer;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import static java.nio.charset.StandardCharsets.UTF_8;

public class AutomaticEncodingHttpServletRequest extends HttpServletRequestWrapper {
    private final HashMap<String, String> encodingCache = new HashMap<String, String>();

    public AutomaticEncodingHttpServletRequest(HttpServletRequest r) {
        super(r);
    }

    public String getHeader(String name) {
        return encode(super.getHeader(name));
    }

    public String getPathInfo() {
        return encode(super.getPathInfo());
    }

    public String getQueryString() {
        return encode(super.getQueryString());
    }

    public String	getRequestURI() {
        return encode(super.getRequestURI());
    }

    public StringBuffer	getRequestURL() {
        return new StringBuffer(getRequestURI());
    }

    public String	getServletPath() {
        return encode(super.getServletPath());
    }

    private String encode(String input) {
      if (input == null) {
        return input;
      }
      if (encodingCache.containsKey(input)) {
        return encodingCache.get(input);
      }

      System.out.println("**** in");
      System.out.println(input);
      String output = encodeProcess(input);
      encodingCache.put(input, output);
      System.out.println(output);

      return output;
    }

    private String encodeProcess(String input) {
      CharsetDecoder decoder = Charset.forName("x-JISAutoDetect").newDecoder()
        .onMalformedInput(CodingErrorAction.REPORT)
        .onUnmappableCharacter(CodingErrorAction.REPORT);
      ByteBuffer inputBuffer = ByteBuffer.wrap(input.getBytes());
      try {
        CharBuffer cb = decoder.decode(inputBuffer);
        if (!decoder.isCharsetDetected()) {
          return input;
        }
        Charset detectedCharset = decoder.detectedCharset();
        CharsetEncoder encoder = UTF_8.newEncoder();
        System.out.println("Convert from");
        System.out.println(detectedCharset);
        ByteBuffer outputBuffer = encoder.encode(cb);
        System.out.println(outputBuffer);
        System.out.println(outputBuffer.toString());
        System.out.println(decoder.toString());
        System.out.println(encoder.toString());
        System.out.println(new String(input.getBytes(), detectedCharset));
        System.out.println(new String(input.getBytes(), UTF_8));
        System.out.println(detectedCharset.decode(inputBuffer).toString());
        System.out.println(new String(detectedCharset.decode(inputBuffer).array()));
        String output = new String(outputBuffer.array(), UTF_8);
        encodingCache.put(input, output);
        return output;
      } catch (CharacterCodingException e) {
        System.out.println("Exception coding exception");
        System.out.println(e);
        return input;
      }
    }
}

