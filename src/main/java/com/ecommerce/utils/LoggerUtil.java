package com.ecommerce.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Centralized logging utility using SLF4J + Logback.
 */
public final class LoggerUtil {

    private static final Logger logger =
        LoggerFactory.getLogger("EcommerceTestFramework");

    private LoggerUtil() {
        throw new UnsupportedOperationException(
            "Utility class - cannot be instantiated"
        );
    }

    public static void info(String message) {
        logger.info(message);
    }

    public static void debug(String message) {
        logger.debug(message);
    }

    public static void warn(String message) {
        logger.warn(message);
    }

    public static void error(String message) {
        logger.error(message);
    }

    public static void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }

    public static void step(String stepDescription) {
        logger.info("[STEP] " + stepDescription);
    }
}
