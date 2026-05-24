package com.ecommerce.constants;

/**
 * Centralized application constants used across the framework.
 */
public final class AppConstants {

    private AppConstants() {
        throw new UnsupportedOperationException(
            "Utility class - cannot be instantiated"
        );
    }

    // ====== Page Titles ======
    public static final String HOME_PAGE_TITLE = "Your Store";
    public static final String LOGIN_PAGE_TITLE = "Account Login";
    public static final String DASHBOARD_PAGE_TITLE = "My Account";

    // ====== URL Paths ======
    public static final String LOGIN_PATH =
        "/index.php?route=account/login";
    public static final String ACCOUNT_PATH =
        "/index.php?route=account/account";
    public static final String LOGOUT_PATH =
        "/index.php?route=account/logout";

    // ====== API Endpoints ======
    public static final String API_LOGIN_ENDPOINT = "/api/login";
    public static final String API_USER_ENDPOINT = "/api/user";

    // ====== Timeouts (in ms) ======
    public static final int SHORT_TIMEOUT = 5000;
    public static final int DEFAULT_TIMEOUT = 15000;
    public static final int LONG_TIMEOUT = 30000;
    public static final int API_TIMEOUT = 10000;

    // ====== Messages ======
    public static final String LOGIN_ERROR_MSG =
        "Warning: No match for E-Mail Address and/or Password.";
    public static final String LOGOUT_SUCCESS_MSG =
        "You have been logged off your account.";

    // ====== Test Data Keys ======
    public static final String VALID_USER_KEY = "validUser";
    public static final String INVALID_USER_KEY = "invalidUser";
    public static final String LOCKED_USER_KEY = "lockedUser";
}
