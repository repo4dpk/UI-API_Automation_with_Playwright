package com.ecommerce.pages;

import com.ecommerce.config.ConfigReader;
import com.ecommerce.constants.AppConstants;
import com.ecommerce.utils.LoggerUtil;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

/**
 * Page Object for the E-Commerce Home Page.
 * Contains locators and actions specific to the homepage.
 */
public class HomePage extends BasePage {

    // ====== Locators ======
    private static final String MY_ACCOUNT_DROPDOWN =
        "a[title='My Account']";
    private static final String LOGIN_LINK =
        "a:has-text('Login')";
    private static final String REGISTER_LINK =
        "a:has-text('Register')";
    private static final String SEARCH_INPUT =
        "input[name='search']";
    private static final String SEARCH_BUTTON =
        "button.btn-default.btn-lg";
    private static final String CART_BUTTON =
        "#cart > button";
    private static final String LOGO =
        "#logo a img";

    public HomePage(Page page) {
        super(page);
    }

    // ====== Actions ======

    @Step("Open E-Commerce Home Page")
    public HomePage openHomePage() {
        String baseUrl = ConfigReader.getInstance().getBaseUrl();
        navigateTo(baseUrl);
        LoggerUtil.info("Home page opened successfully");
        return this;
    }

    @Step("Click on My Account dropdown")
    public HomePage clickMyAccount() {
        click(MY_ACCOUNT_DROPDOWN);
        LoggerUtil.info("My Account dropdown clicked");
        return this;
    }

    @Step("Navigate to Login page from Home page")
    public LoginPage navigateToLoginPage() {
        clickMyAccount();
        click(LOGIN_LINK);
        waitForPageLoad();
        LoggerUtil.info("Navigated to Login page");
        return new LoginPage(page);
    }

    @Step("Navigate to Registration page")
    public HomePage navigateToRegisterPage() {
        clickMyAccount();
        click(REGISTER_LINK);
        waitForPageLoad();
        return this;
    }

    @Step("Search for product: {productName}")
    public HomePage searchProduct(String productName) {
        type(SEARCH_INPUT, productName);
        click(SEARCH_BUTTON);
        waitForPageLoad();
        LoggerUtil.info("Searched for product: " + productName);
        return this;
    }

    // ====== Validations ======

    @Step("Verify Home Page title")
    public boolean isHomePageDisplayed() {
        String title = getPageTitle();
        boolean isDisplayed = title.contains(AppConstants.HOME_PAGE_TITLE);
        LoggerUtil.info("Home page displayed: " + isDisplayed);
        return isDisplayed;
    }

    @Step("Check if logo is visible")
    public boolean isLogoVisible() {
        return isVisible(LOGO);
    }
}
