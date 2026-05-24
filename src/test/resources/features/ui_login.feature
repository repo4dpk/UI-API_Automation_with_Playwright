@ui @login
Feature: Customer Login - UI Tests
    As a registered customer of the E-Commerce platform
    I want to be able to log in to my account
    So that I can access my account dashboard and manage my orders

    Background:
        Given the user is on the home page
        And the user navigates to the login page

    @smoke @positive @SmokeTest
    Scenario: Successful login with valid credentials
        When the user enters valid email "testuser@example.com"
        And the user enters valid password "Test@1234"
        And the user clicks the login button
        Then the user should be redirected to the account dashboard
        And the dashboard page title should contain "My Account"
        And the logout link should be visible

    @smoke @negative @SmokeTest
    Scenario: Login fails with invalid credentials
        When the user enters valid email "invalid@example.com"
        And the user enters valid password "WrongPassword"
        And the user clicks the login button expecting failure
        Then an error message should be displayed
        And the error message should contain "Warning"

    @regression @negative @SmokeTest
    Scenario Outline: Login fails with various invalid inputs
        When the user enters valid email "<email>"
        And the user enters valid password "<password>"
        And the user clicks the login button expecting failure
        Then an error message should be displayed

        Examples:
            | email                | password      |
            | invalid@test.com     | Test@1234     |
            | testuser@example.com | wrongpassword |
            |                      | Test@1234     |
            | testuser@example.com |               |

    @regression @positive @SmokeTest
    Scenario: Login page elements are displayed correctly
        Then the email field should be displayed
        And the password field should be displayed
        And the login button should be displayed

    @regression @positive @SmokeTest
    Scenario: Successful login using test data file
        When the user logs in with "validUser" credentials from test data
        Then the user should be redirected to the account dashboard
        And the dashboard page title should contain "My Account"
