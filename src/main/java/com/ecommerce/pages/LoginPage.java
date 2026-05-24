package com.ecommerce.pages;

import com.ecommerce.constants.AppConstants;
import com.ecommerce.utils.LoggerUtil;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

/**
 * Page Object for the Login Page.
 * Encapsulates all login-related locators and interactions.
 */
public class LoginPage extends BasePage {

    // ====== Locators ======
    private static final String EMAIL_INPUT =
        "#input-email";
    private static final String PASSWORD_INPUT =
        "#input-password";
    private static final String LOGIN_BUTTON =
        "input[value='Login']";
    private static final String FORGOTTEN_PASSWORD_LINK =
        "a:has-text('Forgotten Password')";
    private static final String ERROR_MESSAGE =
        "div.alert.alert-danger";
    private static final String LOGIN_PAGE_HEADING =
        "h2:has-text('Returning Customer')";
    private static final String NEW_CUSTOMER_SECTION =
        "h2:has-text('New Customer')";
    private static final String CONTINUE_BUTTON =
        "a.btn.btn-primary:has-text('Continue')";

    public LoginPage(Page page) {
        super(page);
    }

    // ====== Actions ======

    @Step("Enter email: {email}")
    public LoginPage enterEmail(String email) {
        clearAndType(EMAIL_INPUT, email);
        LoggerUtil.info("Email entered: " + email);
        return this;
    }

    @Step("Enter password")
    public LoginPage enterPassword(String password) {
        clearAndType(PASSWORD_INPUT, password);
        LoggerUtil.info("Password entered: ****");
        return this;
    }

    @Step("Click Login button")
    public DashboardPage clickLoginButton() {
        click(LOGIN_BUTTON);
        waitForPageLoad();
        LoggerUtil.info("Login button clicked");
        return new DashboardPage(page);
    }

    @Step("Click Login button expecting failure")
    public LoginPage clickLoginButtonExpectingFailure() {
        click(LOGIN_BUTTON);
        waitForPageLoad();
        LoggerUtil.info("Login button clicked - expecting failure");
        return this;
    }

    @Step("Perform login with email: {email}")
    public DashboardPage doLogin(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        return clickLoginButton();
    }

    @Step("Attempt login with invalid credentials - email: {email}")
    public LoginPage doInvalidLogin(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        return clickLoginButtonExpectingFailure();
    }

    @Step("Click Forgotten Password link")
    public LoginPage clickForgottenPassword() {
        click(FORGOTTEN_PASSWORD_LINK);
        waitForPageLoad();
        LoggerUtil.info("Forgotten Password link clicked");
        return this;
    }

    // ====== Validations ======

    @Step("Verify Login Page is displayed")
    public boolean isLoginPageDisplayed() {
        boolean isDisplayed = isVisible(LOGIN_PAGE_HEADING)
                              && isVisible(EMAIL_INPUT);
        LoggerUtil.info("Login page displayed: " + isDisplayed);
        return isDisplayed;
    }

    @Step("Verify Login Page title")
    public boolean isLoginPageTitleCorrect() {
        return getPageTitle().contains(AppConstants.LOGIN_PAGE_TITLE);
    }

    @Step("Get login error message")
    public String getErrorMessage() {
        waitForVisible(ERROR_MESSAGE);
        String message = getText(ERROR_MESSAGE);
        LoggerUtil.info("Error message: " + message);
        return message;
    }

    @Step("Check if error message is displayed")
    public boolean isErrorMessageDisplayed() {
        return isVisible(ERROR_MESSAGE);
    }

    @Step("Check if email field is displayed")
    public boolean isEmailFieldDisplayed() {
        return isVisible(EMAIL_INPUT);
    }

    @Step("Check if password field is displayed")
    public boolean isPasswordFieldDisplayed() {
        return isVisible(PASSWORD_INPUT);
    }

    @Step("Check if login button is displayed")
    public boolean isLoginButtonDisplayed() {
        return isVisible(LOGIN_BUTTON);
    }

    @Step("Check if new customer section is visible")
    public boolean isNewCustomerSectionVisible() {
        return isVisible(NEW_CUSTOMER_SECTION);
    }
}
