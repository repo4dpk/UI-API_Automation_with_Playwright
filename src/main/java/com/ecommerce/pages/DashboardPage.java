package com.ecommerce.pages;

import com.ecommerce.constants.AppConstants;
import com.ecommerce.utils.LoggerUtil;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

/**
 * Page Object for the Account Dashboard Page (post-login).
 * Contains actions and validations for the My Account section.
 */
public class DashboardPage extends BasePage {

    // ====== Locators ======
    private static final String DASHBOARD_HEADING =
        "h2:has-text('My Account')";
    private static final String EDIT_ACCOUNT_LINK =
        "a:has-text('Edit your account information')";
    private static final String CHANGE_PASSWORD_LINK =
        "a:has-text('Change your password')";
    private static final String ORDER_HISTORY_LINK =
        "a:has-text('View your order history')";
    private static final String LOGOUT_LINK =
        "//a[text()='Logout' and @class='list-group-item']";
    private static final String MY_ACCOUNT_BREADCRUMB =
        "ul.breadcrumb li:last-child a";
    private static final String WISHLIST_LINK =
        "a:has-text('Modify your wish list')";
    private static final String ADDRESS_BOOK_LINK =
        "a:has-text('Modify your address book entries')";

    public DashboardPage(Page page) {
        super(page);
    }

    // ====== Validations ======

    @Step("Verify Dashboard page is displayed")
    public boolean isDashboardDisplayed() {
        waitForPageLoad();
        boolean isDisplayed = isVisible(DASHBOARD_HEADING);
        LoggerUtil.info("Dashboard displayed: " + isDisplayed);
        return isDisplayed;
    }

    @Step("Verify Dashboard page title")
    public boolean isDashboardTitleCorrect() {
        String title = getPageTitle();
        boolean isCorrect = title.contains(
            AppConstants.DASHBOARD_PAGE_TITLE
        );
        LoggerUtil.info("Dashboard title correct: " + isCorrect
                        + " | Actual: " + title);
        return isCorrect;
    }

    @Step("Verify URL contains account path")
    public boolean isAccountUrlCorrect() {
        String url = getCurrentUrl();
        return url.contains("account/account")
               || url.contains("route=account/account");
    }

    @Step("Check if Edit Account link is visible")
    public boolean isEditAccountLinkVisible() {
        return isVisible(EDIT_ACCOUNT_LINK);
    }

    @Step("Check if Logout link is visible")
    public boolean isLogoutLinkVisible() {
        return isVisible(LOGOUT_LINK);
    }

    // ====== Actions ======

    @Step("Click Logout")
    public void clickLogout() {
        click(LOGOUT_LINK);
        waitForPageLoad();
        LoggerUtil.info("Logout clicked");
    }

    @Step("Navigate to Edit Account")
    public void clickEditAccount() {
        click(EDIT_ACCOUNT_LINK);
        waitForPageLoad();
    }

    @Step("Navigate to Order History")
    public void clickOrderHistory() {
        click(ORDER_HISTORY_LINK);
        waitForPageLoad();
    }

    @Step("Navigate to Change Password")
    public void clickChangePassword() {
        click(CHANGE_PASSWORD_LINK);
        waitForPageLoad();
    }

    @Step("Get Dashboard heading text")
    public String getDashboardHeadingText() {
        return getText(DASHBOARD_HEADING);
    }
}
