@API @regression
Feature: Forgot Password endpoint to get the reset link
  
  Background: 
    Given the path is set to "/auth/forgot-password"
    And there should be a user exists with email "forgotPassword@gmail.com" and pass "Dummy@3000"
    
  Scenario Outline: Respective response for the valid and invalid email
    When the request sent with email "<email>"
    Then the status code should be <statusCode> and success should be <isSuccess>
    And the response message should contains "<response>"

    Examples:
    | email | statusCode | isSuccess | response |
    | forgotPassword@gmail.com | 200 | true | Password reset link sent to your email |
    | | 404 | false | User not found with email |
    | wrongEmail@gmail.com | 404 | false | User not found with email |
