
Feature: User Registration on Basketball England
  As a new user
  I want to register on the website
  So that I can become a supporter

  Scenario Outline: Registration validation with different inputs
    Given the user is on the registration page
    When the user enters "<firstName>" as first name
    And the user enters "<lastName>" as last name
    And the user enters a unique Mailnesia email
    And the user confirms the email
    And the user enters "<dob>" as date of birth
    And the user enters "<password>" as password
    And the user confirms "<confirmPassword>" as password
    And the user selects "<role>" as basketball role
    And the user "<termsAccepted>" to the Terms and Conditions
    And the user agrees to the Code of Ethics and Conduct
    And the user agrees to being over 18 years old
    And the user submits the form
    Then the registration should not be completed
    Examples:
      | firstName | lastName | dob        | password    | confirmPassword | role  | termsAccepted   | expectedOutcome                                                         |
      | Lionel    |          | 01/01/1991 | password123 | password123     | Coach | accepts         | Last Name is required                                                   | # Testar att efternamn är obligatoriskt
      | Lionel    | Messi    | 01/01/1991 | password123 | password456     | Coach | accepts         | Passwords do not match                                                  | # Testar att lösenord och bekräftat lösenord måste matcha
      | Lionel    | Messi    | 01/01/1991 | password123 | password123     | Coach | does not accept | You must confirm that you have read and accepted our Terms and Conditions | # Testar att villkoren måste accepteras


    Scenario Outline: Registration validation with correct input
    Given the user is on the registration page
    When the user enters "<firstName>" as first name
    And the user enters "<lastName>" as last name
    And the user enters a unique Mailnesia email
    And the user confirms the email
    And the user enters "<dob>" as date of birth
    And the user enters "<password>" as password
    And the user confirms "<confirmPassword>" as password
    And the user selects "<role>" as basketball role
    And the user "<termsAccepted>" to the Terms and Conditions
    And the user agrees to the Code of Ethics and Conduct
    And the user agrees to being over 18 years old
    And the user submits the form
    Then "<expectedOutcome>" should be displayed
    Examples:
      | firstName | lastName | dob        | password    | confirmPassword | role  | termsAccepted   | expectedOutcome              |
      | Lionel    | Messi    | 01/01/1991 | password123 | password123     | Coach | accepts         | Account created successfully |
