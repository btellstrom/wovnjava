package com.github.wovnio.wovnjava;

class ApiException extends Exception {
    static final ApiException timeout = new ApiException("timeout");

    ApiException(String message) {
        super(message);
    }
}
