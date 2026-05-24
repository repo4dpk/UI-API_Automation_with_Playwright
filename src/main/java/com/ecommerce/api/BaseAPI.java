package com.ecommerce.api;

import com.ecommerce.config.ConfigReader;
import com.ecommerce.utils.LoggerUtil;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import io.qameta.allure.Step;

import java.util.Map;

/**
 * Base API class providing common HTTP methods using Playwright's
 * built-in API testing support.
 */
public class BaseAPI {

    protected APIRequestContext requestContext;
    protected Playwright playwright;
    private final ConfigReader config;

    public BaseAPI() {
        this.config = ConfigReader.getInstance();
    }

    /**
     * Initialize the API request context with base URL and headers.
     */
    @Step("Initialize API Request Context")
    public void initAPIContext() {
        playwright = Playwright.create();
        requestContext = playwright.request().newContext(
            new APIRequest.NewContextOptions()
                .setBaseURL(config.getApiBaseUrl())
                .setExtraHTTPHeaders(Map.of(
                    "Content-Type", "application/json",
                    "Accept", "application/json",
                        "x-api-key",config.getApiKey()
                ))
        );
        LoggerUtil.info("API context initialized with base URL: "
                        + config.getApiBaseUrl());
    }

    /**
     * Initialize API context with authentication token.
     */
    @Step("Initialize API Request Context with Auth Token")
    public void initAPIContextWithToken(String token) {
        playwright = Playwright.create();
        requestContext = playwright.request().newContext(
            new APIRequest.NewContextOptions()
                .setBaseURL(config.getApiBaseUrl())
                .setExtraHTTPHeaders(Map.of(
                    "Content-Type", "application/json",
                    "Accept", "application/json",
                    "Authorization", "Bearer " + token
                ))
        );
        LoggerUtil.info("API context initialized with auth token");
    }

    // ====== HTTP Methods ======

    @Step("POST request to: {endpoint}")
    protected APIResponse post(String endpoint, Object body) {
        LoggerUtil.info("POST " + endpoint);
        APIResponse response = requestContext.post(endpoint,
            com.microsoft.playwright.options.RequestOptions.create()
                .setData(body)
        );
        logResponse(response);
        return response;
    }

    @Step("GET request to: {endpoint}")
    protected APIResponse get(String endpoint) {
        LoggerUtil.info("GET " + endpoint);
        APIResponse response = requestContext.get(endpoint);
        logResponse(response);
        return response;
    }

    @Step("GET request with params to: {endpoint}")
    protected APIResponse get(String endpoint,
                               Map<String, String> params) {
        LoggerUtil.info("GET " + endpoint + " with params: " + params);
        com.microsoft.playwright.options.RequestOptions options =
            com.microsoft.playwright.options.RequestOptions.create();
        params.forEach(options::setQueryParam);
        APIResponse response = requestContext.get(endpoint, options);
        logResponse(response);
        return response;
    }

    @Step("PUT request to: {endpoint}")
    protected APIResponse put(String endpoint, Object body) {
        LoggerUtil.info("PUT " + endpoint);
        APIResponse response = requestContext.put(endpoint,
            com.microsoft.playwright.options.RequestOptions.create()
                .setData(body)
        );
        logResponse(response);
        return response;
    }

    @Step("DELETE request to: {endpoint}")
    protected APIResponse delete(String endpoint) {
        LoggerUtil.info("DELETE " + endpoint);
        APIResponse response = requestContext.delete(endpoint);
        logResponse(response);
        return response;
    }

    // ====== Utilities ======

    private void logResponse(APIResponse response) {
        LoggerUtil.info("Response Status: " + response.status());
        LoggerUtil.debug("Response Body: " + response.text());
    }

    /**
     * Dispose of the API context and Playwright instance.
     */
    public void dispose() {
        if (requestContext != null) {
            requestContext.dispose();
        }
        if (playwright != null) {
            playwright.close();
        }
        LoggerUtil.info("API context disposed");
    }
}
