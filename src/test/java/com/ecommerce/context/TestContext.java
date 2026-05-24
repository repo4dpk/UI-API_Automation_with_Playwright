package com.ecommerce.context;

import com.ecommerce.api.AuthAPI;
import com.ecommerce.config.PlaywrightFactory;
import com.ecommerce.pages.DashboardPage;
import com.ecommerce.pages.HomePage;
import com.ecommerce.pages.LoginPage;
import com.microsoft.playwright.Page;

/**
 * Test Context class for sharing state between step definitions.
 * Managed by PicoContainer for Cucumber dependency injection.
 *
 * This is the single source of truth for all page objects,
 * browser factory, and shared state during a test scenario.
 */
public class TestContext {

    private PlaywrightFactory playwrightFactory;
    private Page page;

    // Page Objects
    private HomePage homePage;
    private LoginPage loginPage;
    private DashboardPage dashboardPage;

    // API Objects
    private AuthAPI authAPI;

    // Shared State
    private String currentToken;
    private int lastApiStatusCode;
    private String lastApiResponseBody;

    // ====== Playwright Factory ======

    public PlaywrightFactory getPlaywrightFactory() {
        if (playwrightFactory == null) {
            playwrightFactory = new PlaywrightFactory();
        }
        return playwrightFactory;
    }

    public Page getPage() {
        if (page == null) {
            page = getPlaywrightFactory().initBrowser();
        }
        return page;
    }

    // ====== Page Objects (Lazy Initialization) ======

    public HomePage getHomePage() {
        if (homePage == null) {
            homePage = new HomePage(getPage());
        }
        return homePage;
    }

    public LoginPage getLoginPage() {
        if (loginPage == null) {
            loginPage = new LoginPage(getPage());
        }
        return loginPage;
    }

    public DashboardPage getDashboardPage() {
        if (dashboardPage == null) {
            dashboardPage = new DashboardPage(getPage());
        }
        return dashboardPage;
    }

    // ====== API Objects ======

    public AuthAPI getAuthAPI() {
        if (authAPI == null) {
            authAPI = new AuthAPI();
            authAPI.initAPIContext();
        }
        return authAPI;
    }

    // ====== Shared State Getters/Setters ======

    public String getCurrentToken() {
        return currentToken;
    }

    public void setCurrentToken(String token) {
        this.currentToken = token;
    }

    public int getLastApiStatusCode() {
        return lastApiStatusCode;
    }

    public void setLastApiStatusCode(int statusCode) {
        this.lastApiStatusCode = statusCode;
    }

    public String getLastApiResponseBody() {
        return lastApiResponseBody;
    }

    public void setLastApiResponseBody(String responseBody) {
        this.lastApiResponseBody = responseBody;
    }

    // ====== Cleanup ======

    public void tearDown() {
        if (authAPI != null) {
            authAPI.dispose();
        }
        if (playwrightFactory != null) {
            playwrightFactory.tearDown();
        }

        // Reset page object references
        homePage = null;
        loginPage = null;
        dashboardPage = null;
        page = null;
    }
}
