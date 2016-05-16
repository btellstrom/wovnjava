package com.github.wovnio.wovnjava;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.jetbrains.annotations.Contract;

class Logger {
    static final Log log = LogFactory.getLog(WovnServletFilter.class);
    static int debugMode = 0;

    @Contract(pure = true)
    static boolean isDebug() {
        return debugMode > 0;
    }
}
