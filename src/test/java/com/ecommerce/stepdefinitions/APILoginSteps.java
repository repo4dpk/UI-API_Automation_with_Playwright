package com.ecommerce.stepdefinitions;

import com.ecommerce.api.AuthAPI;
import com.ecommerce.context.TestContext;
import com.ecommerce.utils.AllureReportHelper;
import com.ecommerce.utils.LoggerUtil;
import com.microsoft.playwright.APIResponse;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Step definitions for API Login feature scenarios.
 * Uses Playwright's built-in API testing capabilities.
 */
public class APILoginSteps {

    private final TestContext testContext;
    private AuthAPI authAPI;
    private APIResponse apiResponse;

    public APILoginSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    // ====== When Steps ======

    @When("the user sends a login API request with email {string} and password {string}")
    public void theUserSendsALoginAPIRequest(
            String email, String password) {
        authAPI = testContext.getAuthAPI();
        apiResponse = authAPI.login(email, password);

        // Store in context for hooks/reporting
        testContext.setLastApiStatusCode(apiResponse.status());
        testContext.setLastApiResponseBody(apiResponse.text());

        LoggerUtil.step("API login request sent for: " + email);
        AllureReportHelper.attachApiResponse(apiResponse.text());
    }

    // ====== Then Steps ======

    @Then("the API response status code should be {int}")
    public void theAPIResponseStatusCodeShouldBe(int expectedStatus) {
        int actualStatus = apiResponse.status();
        assertEquals(expectedStatus, actualStatus,
            "Expected status code: " + expectedStatus
            + " but got: " + actualStatus);
        LoggerUtil.step("API status code verified: " + actualStatus);
    }

    @Then("the API response should contain a valid token")
    public void theAPIResponseShouldContainAValidToken() {
        String token = authAPI.extractToken();
        assertNotNull(token, "Token should not be null");
        assertFalse(token.isEmpty(), "Token should not be empty");

        testContext.setCurrentToken(token);
        LoggerUtil.step("Valid token received and stored");
    }

    @Then("the API response should contain an error message")
    public void theAPIResponseShouldContainAnErrorMessage() {
        String errorMessage = authAPI.extractErrorMessage();
        assertNotNull(errorMessage,
            "Error message should be present");
        assertFalse(errorMessage.isEmpty(),
            "Error message should not be empty");
        LoggerUtil.step("Error message verified: " + errorMessage);
    }
}
