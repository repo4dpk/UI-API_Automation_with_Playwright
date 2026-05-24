package com.ecommerce.utils;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;

import java.io.ByteArrayInputStream;

/**
 * Utility class for Allure report attachments and metadata.
 */
public final class AllureReportHelper {

    private AllureReportHelper() {
        throw new UnsupportedOperationException(
            "Utility class - cannot be instantiated"
        );
    }

    @Attachment(value = "Screenshot", type = "image/png")
    public static byte[] attachScreenshot(byte[] screenshot) {
        return screenshot;
    }

    @Attachment(value = "{name}", type = "text/plain")
    public static String attachText(String name, String text) {
        return text;
    }

    @Attachment(value = "API Response", type = "application/json")
    public static String attachApiResponse(String responseBody) {
        return responseBody;
    }

    public static void attachScreenshotToReport(
            String name, byte[] screenshot) {
        Allure.addAttachment(
            name,
            "image/png",
            new ByteArrayInputStream(screenshot),
            ".png"
        );
    }

    public static void addEnvironmentInfo(
            String name, String value) {
        Allure.parameter(name, value);
    }

    public static void addStepInfo(String stepName) {
        Allure.step(stepName);
    }

    @Attachment(value = "Page Source", type = "text/html")
    public static String attachPageSource(String pageSource) {
        return pageSource;
    }

    @Attachment(value = "Trace File", type = "application/zip")
    public static byte[] attachTrace(byte[] trace) {
        return trace;
    }
}
