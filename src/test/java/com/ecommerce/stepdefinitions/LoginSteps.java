package com.ecommerce.stepdefinitions;

import com.ecommerce.context.TestContext;
import com.ecommerce.pages.DashboardPage;
import com.ecommerce.pages.HomePage;
import com.ecommerce.pages.LoginPage;
import com.ecommerce.utils.LoggerUtil;
import com.ecommerce.utils.TestDataProvider;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Step definitions for UI Login feature scenarios.
 * Uses TestContext for dependency injection of page objects.
 */
public class LoginSteps {

    private final TestContext testContext;
    private HomePage homePage;
    private LoginPage loginPage;
    private DashboardPage dashboardPage;

    public LoginSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    // ====== Given Steps ======

    @Given("the user is on the home page")
    public void theUserIsOnTheHomePage() {
        homePage = testContext.getHomePage();
        homePage.openHomePage();
        assertTrue(homePage.isHomePageDisplayed(),
            "Home page should be displayed");
        LoggerUtil.step("User is on the home page");
    }

    @Given("the user navigates to the login page")
    public void theUserNavigatesToTheLoginPage() {
        loginPage = homePage.navigateToLoginPage();
        assertTrue(loginPage.isLoginPageDisplayed(),
            "Login page should be displayed");
        LoggerUtil.step("User navigated to login page");
    }

    // ====== When Steps ======

    @When("the user enters valid email {string}")
    public void theUserEntersValidEmail(String email) {
        loginPage.enterEmail(email);
        LoggerUtil.step("Email entered: " + email);
    }

    @When("the user enters valid password {string}")
    public void theUserEntersValidPassword(String password) {
        loginPage.enterPassword(password);
        LoggerUtil.step("Password entered");
    }

    @When("the user clicks the login button")
    public void theUserClicksTheLoginButton() {
        dashboardPage = loginPage.clickLoginButton();
        LoggerUtil.step("Login button clicked");
    }

    @When("the user clicks the login button expecting failure")
    public void theUserClicksTheLoginButtonExpectingFailure() {
        loginPage = loginPage.clickLoginButtonExpectingFailure();
        LoggerUtil.step("Login button clicked - expecting failure");
    }

    @When("the user logs in with {string} credentials from test data")
    public void theUserLogsInWithCredentialsFromTestData(
            String userType) {
        Map<String, String> credentials =
            TestDataProvider.getUserCredentials(userType);
        dashboardPage = loginPage.doLogin(
            credentials.get("email"),
            credentials.get("password")
        );
        LoggerUtil.step(
            "User logged in with test data credentials: " + userType
        );
    }

    // ====== Then Steps ======

    @Then("the user should be redirected to the account dashboard")
    public void theUserShouldBeRedirectedToTheAccountDashboard() {
        dashboardPage = testContext.getDashboardPage();
        assertTrue(dashboardPage.isDashboardDisplayed(),
            "Dashboard should be displayed after successful login");
        LoggerUtil.step("User is redirected to account dashboard");
    }

    @Then("the dashboard page title should contain {string}")
    public void theDashboardPageTitleShouldContain(
            String expectedTitle) {
        String actualTitle = dashboardPage.getPageTitle();
        assertTrue(actualTitle.contains(expectedTitle),
            "Dashboard title should contain '" + expectedTitle
            + "' but was '" + actualTitle + "'");
        LoggerUtil.step("Dashboard title verified: " + actualTitle);
    }

    @Then("the logout link should be visible")
    public void theLogoutLinkShouldBeVisible() {
        assertTrue(dashboardPage.isLogoutLinkVisible(),
            "Logout link should be visible on dashboard");
        LoggerUtil.step("Logout link is visible");
    }

    @Then("an error message should be displayed")
    public void anErrorMessageShouldBeDisplayed() {
        assertTrue(loginPage.isErrorMessageDisplayed(),
            "Error message should be displayed for invalid login");
        LoggerUtil.step("Error message is displayed");
    }

    @Then("the error message should contain {string}")
    public void theErrorMessageShouldContain(String expectedText) {
        String actualMessage = loginPage.getErrorMessage();
        assertTrue(actualMessage.contains(expectedText),
            "Error message should contain '" + expectedText
            + "' but was '" + actualMessage + "'");
        LoggerUtil.step("Error message verified: " + actualMessage);
    }

    @Then("the email field should be displayed")
    public void theEmailFieldShouldBeDisplayed() {
        assertTrue(loginPage.isEmailFieldDisplayed(),
            "Email field should be displayed");
    }

    @And("the password field should be displayed")
    public void thePasswordFieldShouldBeDisplayed() {
        assertTrue(loginPage.isPasswordFieldDisplayed(),
            "Password field should be displayed");
    }

    @And("the login button should be displayed")
    public void theLoginButtonShouldBeDisplayed() {
        assertTrue(loginPage.isLoginButtonDisplayed(),
            "Login button should be displayed");
    }
}
