
@API @regression
Feature: Registration of the user profile

  Background:
    Given the path is set to "/auth/register"


  @smoke
  Scenario: Successful registration with valid data
    When the request payload is sent with following data:
      | email | manoj55802@gmail.com |
      | fullName | Manoj A |
      | password | Dummy@3000 |
      | role | Student |
    Then the status code should be 201 and success should be true
    And the response message should contains "User registered successfully"


  Scenario Outline: Unsuccessful registration with missing and invalid parameters
    When sent request with "<email>", "<fullName>", "<password>", "<confirmPassword>", "<role>"
    Then the status code should be 400 and success should be false
    And the response message should contains "<response>"

    Examples:
      | email | fullName | password | confirmPassword |  role | response |
      | emptyfullname@gmail.com |  | Dummy@3000 | Dummy@3000 | Student | name should not be empty |
      |  | Empty Email | Dummy@3000 | Dummy@3000 | Student | Invalid email format                 |
      | ll# | Wrong Email Format | Dummy@3000 | Dummy@3000 | Student | Invalid email format       |
      | pwdvalidation@c.com | Password Validation | Z1xcvbnmui | Z1xcvbnmui | Student | Password must contain at least one special character |
      | pwdvalidation@c.com | Password Validation | Z@xcvbnmui | Z@xcvbnmui | Student | Password must contain at least one digit |
      | pwdvalidation@c.com | Password Validation | Z@x1 | Z@x1 | Student |  Password must be at least 8 characters long |
      | wrongcfmPwd@gmail.com | Password Validation | ummy@3000 | ummy@3000 | Student | Password must contain at least one uppercase letter |
      | wrongcfmPwd@gmail.com | Wrong confirm password | Dummy@3000 | Wrong@3000 | Student | Passwords do not match                    |
      | manoj55802@gmail.com  | Duplicate email        | Dummy@3000 | Dummy@3000 | Admin   | Email already registered                  |

