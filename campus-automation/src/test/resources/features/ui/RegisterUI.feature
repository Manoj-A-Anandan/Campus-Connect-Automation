@UI @regression
Feature: User Registration

  Background:
    Given user is on the Login page
    And user navigates to the Registration page


  @smoke
  Scenario: Successful registration with valid details
    When user registers with:
      | email           | manoj55802Login@gmail.com |
      | fullName        | Manoj A                  |
      | password        | Dummy@3000               |
      | confirmPassword | Dummy@3000               |
      | role            | Student                  |
    And clicks on the Register button
    Then the user should be redirected to the Dashboard page


  Scenario Outline: Registration validation errors

    When user registers with:
      | email           | <email>           |
      | fullName        | <fullName>        |
      | password        | <password>        |
      | confirmPassword | <confirmPassword> |
      | role            | <role>            |
    And clicks on the Register button
    Then an error message should contain "<response>"

    Examples:
      | email                           | fullName            | password    | confirmPassword | role    | response                                          |
      |                                 | Empty Email         | Dummy@3000  | Dummy@3000      | Student | please fill                                       |
      | wrongFormat                     | Wrong Email Format  | Dummy@3000  | Dummy@3000      | Student | please include                                    |
      | emptyName@gmail.com             |                     | Dummy@3000  | Dummy@3000      | Student | please fill                                       |
      | singlecharacter@gmail.com       | a                   | Dummy@3000  | Dummy@3000      | Student | min character                                     |
      | specialCharacter@email.com      | Manoj@              | Dummy@3000  | Dummy@3000      | Student | name should contains character only               |
      | hugePayload@gmail.com           | aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa | Dummy@3000 | Dummy@3000 | Student | max character |
      | emptyPassword@gmail.com         | Empty Password      |             | Dummy@3000      | Student | please fill                                       |
      | emptyConfirmPassword@gmail.com  | Empty Confirm Password | Dummy@3000 |             | Student | please fill                                       |
      | pwdvalidation@gmail.com         | Password Validation | Z1xcvbnmui  | Z1xcvbnmui      | Student | Password must contain at least one special character |
      | pwdvalidation@gmail.com         | Password Validation | Z@xcvbnmui  | Z@xcvbnmui      | Student | Password must contain at least one digit          |
      | pwdvalidation@gmail.com         | Password Validation | ummy@3000   | ummy@3000       | Student | Password must contain at least one uppercase letter |
      | pwdvalidation@gmail.com         | Password Validation | Z@x1        | Z@x1            | Student | Password must be at least 8 characters long       |
      | wrongcfmPwd@gmail.com           | Wrong Confirm Password | Dummy@3000 | Wrong@3000   | Student | Passwords do not match                            |
      | manoj55802Login@gmail.com       | Duplicate Email     | Dummy@3000  | Dummy@3000      | Student | Email already registered                          |