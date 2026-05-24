package com.ecommerce.config;

import com.ecommerce.utils.LoggerUtil;
import com.microsoft.playwright.*;
import com.microsoft.playwright.BrowserType.LaunchOptions;

import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Factory class responsible for managing Playwright browser lifecycle.
 * Handles browser creation, context management, page creation,
 * and optional tracing.
 */
public class PlaywrightFactory {

    private Playwright playwright;
    private Browser browser;
    private BrowserContext browserContext;
    private Page page;
    private final ConfigReader config;

    public PlaywrightFactory() {
        this.config = ConfigReader.getInstance();
    }

    /**
     * Initializes Playwright and launches the configured browser.
     * Sets up browser context with viewport, tracing, and other options.
     *
     * @return Page instance ready for interaction
     */
    public Page initBrowser() {
        String browserName = config.getBrowser().toLowerCase();
        boolean headless = config.isHeadless();
        double slowMo = config.getSlowMo();

        LoggerUtil.info(String.format(
            "Initializing browser: %s | Headless: %s | SlowMo: %.0fms",
            browserName, headless, slowMo
        ));

        playwright = Playwright.create();

        LaunchOptions launchOptions = new LaunchOptions()
                .setHeadless(headless)
                .setSlowMo(slowMo);

        // Add common browser arguments for CI stability
        switch (browserName) {
            case "chromium", "chrome" -> {
                launchOptions.setArgs(Arrays.asList(
                    "--disable-dev-shm-usage",
                    "--no-sandbox",
                    "--disable-gpu"
                ));
                if ("chrome".equals(browserName)) {
                    launchOptions.setChannel("chrome");
                }
                browser = playwright.chromium().launch(launchOptions);
            }
            case "firefox" -> {
                browser = playwright.firefox().launch(launchOptions);
            }
            case "webkit", "safari" -> {
                browser = playwright.webkit().launch(launchOptions);
            }
            default -> throw new IllegalArgumentException(
                "Unsupported browser: " + browserName
            );
        }

        // Configure browser context
        Browser.NewContextOptions contextOptions =
            new Browser.NewContextOptions()
                .setViewportSize(1920, 1080)
                .setIgnoreHTTPSErrors(true);

        browserContext = browser.newContext(contextOptions);

        // Enable tracing if configured
        if (config.isTracingEnabled()) {
            browserContext.tracing().start(
                new Tracing.StartOptions()
                    .setScreenshots(true)
                    .setSnapshots(true)
                    .setSources(true)
            );
            LoggerUtil.info("Tracing enabled for this session");
        }

        // Set default timeouts
        browserContext.setDefaultTimeout(config.getDefaultTimeout());

        page = browserContext.newPage();

        LoggerUtil.info("Browser initialized successfully");
        return page;
    }

    /**
     * Captures a screenshot and returns the byte array
     * (useful for Allure attachments).
     */
    public byte[] takeScreenshot() {
        if (page != null && !page.isClosed()) {
            return page.screenshot(
                new Page.ScreenshotOptions().setFullPage(true)
            );
        }
        return new byte[0];
    }

    /**
     * Saves trace file for debugging failed scenarios.
     */
    public void saveTrace(String scenarioName) {
        if (config.isTracingEnabled() && browserContext != null) {
            String traceFileName = "target/traces/"
                + scenarioName.replaceAll("[^a-zA-Z0-9]", "_")
                + ".zip";
            browserContext.tracing().stop(
                new Tracing.StopOptions()
                    .setPath(Paths.get(traceFileName))
            );
            LoggerUtil.info("Trace saved: " + traceFileName);
        }
    }

    /**
     * Returns the APIRequestContext for making API calls
     * using Playwright's built-in API support.
     */
    public APIRequestContext createAPIRequestContext() {
        if (playwright == null) {
            playwright = Playwright.create();
        }

        return playwright.request().newContext(
            new APIRequest.NewContextOptions()
                .setBaseURL(config.getApiBaseUrl())
                .setExtraHTTPHeaders(java.util.Map.of(
                    "Content-Type", "application/json",
                    "Accept", "application/json"
                ))
        );
    }

    /**
     * Cleans up all Playwright resources.
     */
    public void tearDown() {
        LoggerUtil.info("Tearing down Playwright resources...");
        if (page != null && !page.isClosed()) {
            page.close();
        }
        if (browserContext != null) {
            browserContext.close();
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
        LoggerUtil.info("Playwright resources cleaned up");
    }

    // Getters
    public Page getPage() { return page; }
    public Browser getBrowser() { return browser; }
    public BrowserContext getBrowserContext() { return browserContext; }
    public Playwright getPlaywright() { return playwright; }
}
