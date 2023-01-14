Feature: End to End test for Trello

  Scenario: Authorized memeber can create an organization
    Given create a new organization
    Then Get Organizations for a member
    And Create a board inside an organization
    Then Get boards in an organization
    And Create a new list
    Then Get all lists on a board
    Then Archive a list
    Then Delete a board
    Then Delete an organization




