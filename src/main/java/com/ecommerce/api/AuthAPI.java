package com.ecommerce.api;

import com.ecommerce.constants.AppConstants;
import com.ecommerce.utils.LoggerUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIResponse;
import io.qameta.allure.Step;

import java.util.Map;

/**
 * API class for authentication-related endpoints.
 * Encapsulates login, token refresh, and user info API calls.
 */
public class AuthAPI extends BaseAPI {

    private final ObjectMapper objectMapper;
    private APIResponse lastResponse;

    public AuthAPI() {
        super();
        this.objectMapper = new ObjectMapper();
    }

    @Step("API Login with email: {email}")
    public APIResponse login(String email, String password) {
        Map<String, String> loginPayload = Map.of(
            "email", email,
            "password", password
        );

        LoggerUtil.info("API Login request for: " + email);
        lastResponse = post(AppConstants.API_LOGIN_ENDPOINT, loginPayload);
        return lastResponse;
    }

    @Step("Get authenticated user info")
    public APIResponse getUserInfo(String token) {
        initAPIContextWithToken(token);
        lastResponse = get(AppConstants.API_USER_ENDPOINT);
        return lastResponse;
    }

    @Step("Get response status code")
    public int getStatusCode() {
        return lastResponse != null ? lastResponse.status() : -1;
    }

    @Step("Get response body as string")
    public String getResponseBody() {
        return lastResponse != null ? lastResponse.text() : "";
    }

    @Step("Parse response body as JSON")
    public JsonNode getResponseAsJson() {
        try {
            return objectMapper.readTree(getResponseBody());
        } catch (Exception e) {
            LoggerUtil.error("Failed to parse response as JSON", e);
            return null;
        }
    }

    @Step("Extract token from login response")
    public String extractToken() {
        JsonNode json = getResponseAsJson();
        if (json != null && json.has("token")) {
            String token = json.get("token").asText();
            LoggerUtil.info("Token extracted successfully");
            return token;
        }
        LoggerUtil.warn("Token not found in response");
        return null;
    }

    @Step("Extract error message from response")
    public String extractErrorMessage() {
        JsonNode json = getResponseAsJson();
        if (json != null && json.has("message")) {
            return json.get("message").asText();
        }
        if (json != null && json.has("error")) {
            return json.get("error").asText();
        }
        return "No error message found";
    }

    @Step("Check if login was successful")
    public boolean isLoginSuccessful() {
        return lastResponse != null
               && lastResponse.status() == 200
               && lastResponse.ok();
    }

    public APIResponse getLastResponse() {
        return lastResponse;
    }
}
