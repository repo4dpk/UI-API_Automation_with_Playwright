package com.ecommerce.pages;

import com.ecommerce.constants.AppConstants;
import com.ecommerce.utils.LoggerUtil;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;

/**
 * Base page class providing common interactions and wait utilities.
 * All page objects extend this class to inherit reusable methods.
 */
public abstract class BasePage {

    protected final Page page;

    protected BasePage(Page page) {
        this.page = page;
    }

    // ====== Navigation ======

    @Step("Navigate to URL: {url}")
    protected void navigateTo(String url) {
        LoggerUtil.info("Navigating to: " + url);
        page.navigate(url);
        page.waitForLoadState();
    }

    // ====== Element Interactions ======

    @Step("Click on element: {selector}")
    protected void click(String selector) {
        LoggerUtil.info("Clicking element: " + selector);
        waitForVisible(selector);
        page.locator(selector).click();
    }

    @Step("Type '{text}' into element: {selector}")
    protected void type(String selector, String text) {
        LoggerUtil.info("Typing into element: " + selector);
        waitForVisible(selector);
        page.locator(selector).fill(text);
    }

    @Step("Clear and type '{text}' into element: {selector}")
    protected void clearAndType(String selector, String text) {
        LoggerUtil.info("Clear and type into: " + selector);
        waitForVisible(selector);
        Locator locator = page.locator(selector);
        locator.clear();
        locator.fill(text);
    }

    @Step("Get text from element: {selector}")
    protected String getText(String selector) {
        waitForVisible(selector);
        String text = page.locator(selector).textContent();
        LoggerUtil.info("Got text: " + text + " from: " + selector);
        return text != null ? text.trim() : "";
    }

    @Step("Get input value from element: {selector}")
    protected String getInputValue(String selector) {
        waitForVisible(selector);
        return page.locator(selector).inputValue();
    }

    // ====== State Checks ======

    @Step("Check if element is visible: {selector}")
    protected boolean isVisible(String selector) {
        try {
            return page.locator(selector).isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Check if element is enabled: {selector}")
    protected boolean isEnabled(String selector) {
        return page.locator(selector).isEnabled();
    }

    @Step("Get page title")
    public String getPageTitle() {
        String title = page.title();
        LoggerUtil.info("Page title: " + title);
        return title;
    }

    @Step("Get current URL")
    public String getCurrentUrl() {
        String url = page.url();
        LoggerUtil.info("Current URL: " + url);
        return url;
    }

    // ====== Wait Utilities ======

    protected void waitForVisible(String selector) {
        page.locator(selector).waitFor(
            new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(AppConstants.DEFAULT_TIMEOUT)
        );
    }

    protected void waitForHidden(String selector) {
        page.locator(selector).waitFor(
            new Locator.WaitForOptions()
                .setState(WaitForSelectorState.HIDDEN)
                .setTimeout(AppConstants.DEFAULT_TIMEOUT)
        );
    }

    protected void waitForNavigation(Runnable action) {

        page.waitForURL("https://example.com");
        page.waitForURL("https://example.com", new Page.WaitForURLOptions().setTimeout(5000));

        // page.waitForURL("**/expectedPath", () -> {
        //     action.run();
        // });
        page.click("button#submit");



    }

    protected void waitForPageLoad() {
        page.waitForLoadState();
    }

    // ====== Dropdown & Select ======

    @Step("Select option '{value}' from dropdown: {selector}")
    protected void selectOption(String selector, String value) {
        LoggerUtil.info("Selecting option: " + value
                        + " from: " + selector);
        page.locator(selector).selectOption(value);
    }

    // ====== Scroll ======

    @Step("Scroll to element: {selector}")
    protected void scrollToElement(String selector) {
        page.locator(selector).scrollIntoViewIfNeeded();
    }

    // ====== Screenshot ======

    public byte[] captureScreenshot() {
        return page.screenshot(
            new Page.ScreenshotOptions().setFullPage(true)
        );
    }
}
