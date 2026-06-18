@api @auth @reset-password
Feature: Reset password

  Background:
    Given there should be a user exists with email "resetPassword@gmail.com" and pass "Dummy@3000"

  Scenario: User resets password with a valid reset token
    Given the path is set to "/auth/forgot-password"
    When the request sent with email "resetPassword@gmail.com"
    Then the status code should be 200 and success should be true
    And the response message should contains "Password reset link sent to your email"

    When I retrieve the latest reset token for "resetPassword@gmail.com" from the test endpoint
    Then the status code should be 200 and success should be true
    And the response message should contains "Token retrieved successfully"

    When the validate token request is sent with the retrieved token
    Then the status code should be 200 and success should be true
    And the response message should contains "Token is valid"

    When the reset password request is sent with the retrieved token, password "NewPassword@123", and confirmPassword "NewPassword@123"
    Then the status code should be 200 and success should be true
    And the response message should contains "Password reset successfully"

    Given the path is set to "/auth/login"
    When sent request with "resetPassword@gmail.com", "NewPassword@123"
    Then the status code should be 200 and success should be true
    And the response message should contains "Login successful"

  Scenario: Forgot password fails for an unregistered email
    Given the path is set to "/auth/forgot-password"
    When the request sent with email "wrongEmail@gmail.com"
    Then the status code should be 404 and success should be false
    And the response message should contains "User not found with email"

  Scenario Outline: Reset password rejects invalid password input
    Given the path is set to "/auth/forgot-password"
    When the request sent with email "resetPassword@gmail.com"
    Then the status code should be 200 and success should be true
    And the response message should contains "Password reset link sent to your email"

    When I retrieve the latest reset token for "resetPassword@gmail.com" from the test endpoint
    Then the status code should be 200 and success should be true

    When the reset password request is sent with the retrieved token, password "<newPassword>", and confirmPassword "<confirmPassword>"
    Then the status code should be 400 and success should be false
    And the response message should contains "<response>"

    Examples:
      | newPassword     | confirmPassword | response                                  |
      | NewPassword@123 | mismatchPass    | Passwords do not match                    |
      | short           | short           | Password must be at least 8 characters long |
