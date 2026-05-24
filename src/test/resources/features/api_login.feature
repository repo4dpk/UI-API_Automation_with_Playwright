@api @login
Feature: Customer Login - API Tests
    As an API consumer
    I want to authenticate via the login API endpoint
    So that I can receive a valid token for subsequent API calls

    @smoke @positive @SmokeTest
    Scenario: Successful API login returns token
        When the user sends a login API request with email "eve.holt@reqres.in" and password "testPwssword"
        Then the API response status code should be 200
        And the API response should contain a valid token

    @smoke @negative @SmokeTest
    Scenario: API login fails with invalid credentials
        When the user sends a login API request with email "invalid@test.com" and password "WrongPassword"
        Then the API response status code should be 400
        And the API response should contain an error message

    @regression @negative @SmokeTest
    Scenario: API login fails with missing email
        When the user sends a login API request with email "" and password "cityslicka"
        Then the API response status code should be 400

    @regression @negative @SmokeTest
    Scenario: API login fails with missing password
        When the user sends a login API request with email "testuser@example.com" and password ""
        Then the API response status code should be 400
