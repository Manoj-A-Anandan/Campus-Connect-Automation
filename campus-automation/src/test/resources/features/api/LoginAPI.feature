
Feature: Login Functionality for the existing user

  Background:
    Given the path is set to "/auth/login"
    And there should be a user exists with email "login@gmail.com" and pass "Dummy@3000"


  Scenario: Successful login with valid credentials
    When sent request with "login@gmail.com", "Dummy@3000"
    Then the status code should be 200 and success should be true
    And the response message should contains "Login Successful"
    And the response data should match the registration JSON schema


  Scenario Outline: Unsuccessful login with missing and invalid parameters
    When sent request with "<email>", "<password>"
    Then the status code should be 400 and success should be false
    And the response message should contains "<response>"

    Examples:
      | email | password | response |
      | login@gmail.com | incorrectPassword | Invalid email or password |
      | invalidemail@gmail.com | Dummy@3000 | Invalid email or password |
      |  | Dummy@3000 | Invalid email or password |
      | login@gmail.com |  | Invalid email or password |
      |  |  | Invalid email or password |
      | wrongEmail@98 | wrongPass | Invalid email or password |