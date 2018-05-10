package com.github.wovnio.wovnjava;

class APIException extends Exception {
    static final APIException timeout = new APIException("timeout");

    APIException(String message) {
        super(message);
    }
}
