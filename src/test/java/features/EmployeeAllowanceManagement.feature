Feature: Employee Allowance Management where employee can :
  [Allowance Summary] View claim balance and apply new claim
  [Allowance History] View past allowance claimed
  [Pending Claim] View pending claim and cancel claim

  Background:
    Given User logs in using 'awtestingbot@outlook.com' and 'Perfume@123' credentials
    And employee click on Allowance Management
    And employee can see Allowance Summary header

    Scenario Outline: Verify that employee able to create new claim
      When employee click on New Claim button
      And employee fill all the fields <claimType>, <billAmount>, <visitDate>, <claimDesc>, <filePath>
      Then employee click on create button
      And toast message shall display

    Examples:
    | claimType      | billAmount | visitDate       | claimDesc                  | filePath                                                   |
    |"Optical / Dental Claim" | "1.00"     | "02 March 2026" | "Automated claim creation" | "C:\Users\ainin\OneDrive\Documents\test-data\test-png.png" |
    |"Medical Claim" | "2.00"     | "02 March 2026" | "Automated claim creation" | "C:\Users\ainin\OneDrive\Documents\test-data\test-png.png" |
    |"Other Claim" | "3.00"     | "02 March 2026" | "Automated claim creation" | "C:\Users\ainin\OneDrive\Documents\test-data\test-png.png" |

    Scenario Outline: Verify that employee able to sort allowance history table
      When employee click on Allowance History
      And User clicks on header sorting icon for column <columnName>
      And Column <columnName> is sorted in descending order
      And User clicks on header sorting icon for column <columnName>
      Then Column <columnName> is sorted in ascending order

      Examples:
        |columnName      |
        |"Claim Type"    |
        |"Submit Date"   |
        |"Amount (RM)"   |
        |"Status"        |

    Scenario: Verify that employee able to view the allowance details
      When employee click on Allowance History
      And employee click on row action button
      Then the details shall be the same as the row

    Scenario: Verify that employee able to view pending claim details
      When employee click on Pending Claim
      And employee click on row action button
      And employee select View
      Then the details shall be the same as the row in pending claim table

    Scenario: Verify that employee able to cancel applied claim
      When employee click on Pending Claim
      And employee click on row action button
      And employee select View
      And employee view status
      And employee close the side panel
      And employee click on row action button
      And employee select Cancel
      And cancel claim modal shall display
      Then employee click Confirm button
      And employee click on Allowance History
      And the claim should display in Allowance History with status cancelled

