package com.ecommerce.stepdefinitions;

import com.ecommerce.context.TestContext;
import com.ecommerce.utils.AllureReportHelper;
import com.ecommerce.utils.LoggerUtil;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

/**
 * Cucumber Hooks for test lifecycle management.
 * Handles setup, teardown, screenshot capture on failure, etc.
 */
public class Hooks {

    private final TestContext testContext;

    public Hooks(TestContext testContext) {
        this.testContext = testContext;
    }

    @Before(order = 0)
    public void beforeScenario(Scenario scenario) {
        LoggerUtil.info("========================================");
        LoggerUtil.info("STARTING SCENARIO: " + scenario.getName());
        LoggerUtil.info("Tags: " + scenario.getSourceTagNames());
        LoggerUtil.info("========================================");
    }

    @Before(value = "@ui", order = 1)
    public void beforeUIScenario(Scenario scenario) {
        LoggerUtil.info("Initializing browser for UI scenario...");
        // Browser is lazily initialized via TestContext.getPage()
    }

    @Before(value = "@api", order = 1)
    public void beforeAPIScenario(Scenario scenario) {
        LoggerUtil.info("Initializing API context for API scenario...");
        // API context is lazily initialized via TestContext.getAuthAPI()
    }

    @AfterStep
    public void afterStep(Scenario scenario) {
        // Capture screenshot on step failure for UI tests
        if (scenario.isFailed()
                && scenario.getSourceTagNames().contains("@ui")) {
            captureScreenshot(scenario);
        }
    }

    @After(value = "@ui", order = 1)
    public void afterUIScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            captureScreenshot(scenario);

            // Save trace for debugging if enabled
            testContext.getPlaywrightFactory()
                       .saveTrace(scenario.getName());

            LoggerUtil.error("UI Scenario FAILED: " + scenario.getName());
        } else {
            LoggerUtil.info("UI Scenario PASSED: " + scenario.getName());
        }
    }

    @After(value = "@api", order = 1)
    public void afterAPIScenario(Scenario scenario) {
        // Attach last API response for debugging
        String responseBody = testContext.getLastApiResponseBody();
        if (responseBody != null && !responseBody.isEmpty()) {
            AllureReportHelper.attachApiResponse(responseBody);
        }

        if (scenario.isFailed()) {
            LoggerUtil.error(
                "API Scenario FAILED: " + scenario.getName()
            );
        } else {
            LoggerUtil.info(
                "API Scenario PASSED: " + scenario.getName()
            );
        }
    }

    @After(order = 0)
    public void afterScenario(Scenario scenario) {
        LoggerUtil.info("========================================");
        LoggerUtil.info("FINISHED SCENARIO: " + scenario.getName()
                        + " | Status: " + scenario.getStatus());
        LoggerUtil.info("========================================");

        // Cleanup all resources
        testContext.tearDown();
    }

    private void captureScreenshot(Scenario scenario) {
        try {
            byte[] screenshot = testContext.getPlaywrightFactory()
                                           .takeScreenshot();
            if (screenshot.length > 0) {
                scenario.attach(screenshot, "image/png",
                    "Screenshot_" + scenario.getName());
                AllureReportHelper.attachScreenshot(screenshot);
                LoggerUtil.info("Screenshot captured for: "
                                + scenario.getName());
            }
        } catch (Exception e) {
            LoggerUtil.error("Failed to capture screenshot", e);
        }
    }
}
