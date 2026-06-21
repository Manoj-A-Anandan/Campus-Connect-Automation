@UI @regression
Feature: User Login

  Background:
    Given user is on the Login page
    And there should be a user exists with email "loginUI@gmail.com" and pass "Dummy@3000"

  @smoke
  Scenario: Successful login with valid credentials
    When user logs in with:
      | email    | loginUI@gmail.com |
      | password | Dummy@3000        |
    And clicks on the Login button
    Then the user should be redirected to the Dashboard page

  Scenario Outline: Login validation errors
    When user logs in with:
      | email    | <email>    |
      | password | <password> |
    And clicks on the Login button
    Then a login error message should contain "<response>"

    Examples:
      | email                  | password          | response                  |
      | loginUI@gmail.com        | incorrectPassword | Invalid email or password |
      | invalidemail@gmail.com | Dummy@3000        | Invalid email or password |
      |                        | Dummy@3000        | please fill               |
      | loginUI@gmail.com        |                   | please fill               |
      | wrongEmail             | wrongPass         | please include            |

  Scenario: Verify route guard redirection for unauthenticated access
    Given user navigates directly to "/dashboard"
    Then the user should be redirected to the Login page

  Scenario: Log out from active session
    When user logs in with:
      | email    | loginUI@gmail.com |
      | password | Dummy@3000        |
    And clicks on the Login button
    Then the user should be redirected to the Dashboard page
    When the user clicks the logout button
    Then the user should be redirected to the Login page
    And the localStorage "token" should be deleted
