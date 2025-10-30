Feature: Dashboard Feature
  This feature deals with all the components in the application

  #TC-A016
  Scenario Outline: Verify User can successfully lands on the dashboard after login
    Given user on the login page
    And the email <email>
    And the password <password>
    When user clicked login
    Then user see the dashboard

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-U017
  Scenario Outline: Verify User's name and greeting are displayed correctly.
    Given user on the login page
    And the email <email>
    And the password <password>
    When user clicked login
    Then user see the dashboard
    And user see the username and greeting

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-U018
  Scenario Outline: Verify the current date is displayed correctly.
    Given user on the login page
    And the email <email>
    And the password <password>
    When user clicked login
    Then user see the dashboard
    And user see the current date

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |


  #TC-U019
  Scenario Outline: Verify “Announcements” section displays recent updates/messages posted by HR/Admin.
    Given user on the login page
    And the email <email>
    And the password <password>
    When user clicked login
    Then user see the dashboard
    And user see the announcements section

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-U020
  Scenario Outline: Verify "Holiday this Month” section shows relevant public holidays with dates and states.
    Given user on the login page
    And the email <email>
    And the password <password>
    When user clicked login
    Then user see the dashboard
    And user see the holiday section

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-U021
  Scenario Outline: Verify “Pending Claim” count reflects the actual pending claims.
    Given user on the login page
    And the email <email>
    And the password <password>
    When user clicked login
    Then user see the dashboard
    When user notes the Pending Claim count from Dashboard
    And user navigates to Pending Claim Page
    Then the number of claims listed should match the Pending Claim count

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-U022
  Scenario Outline: Verify “Pending Leave” count is accurate.
    Given user on the login page
    And the email <email>
    And the password <password>
    When user clicked login
    Then user see the dashboard
    When user notes the Pending Leave count from Dashboard
    And user navigates to Pending Leave Page
    Then the number of leaves listed should match the Pending Leave count

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-U023
  Scenario Outline: Verify user can click on "View All" link on Leave Balance section.
    Given user on the login page
    And the email <email>
    And the password <password>
    When user clicked login
    Then user see the dashboard
    And user see the leave balance section
    And user click view all on leave

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-U024
  Scenario Outline: Verify user can click on "View All" link on Upcoming Leave section.
    Given user on the login page
    And the email <email>
    And the password <password>
    When user clicked login
    Then user see the dashboard
    And user see the upcoming leave section
    And user click view all on upcoming

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-U025
  Scenario Outline: Verify user can click on "Logout" button
    Given user on the login page
    And the email <email>
    And the password <password>
    When user clicked login
    Then user see the dashboard
    And user click logout

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-U026
  Scenario Outline: Verify user can click on "Leave Management" sidebar
    Given user on the login page
    And the email <email>
    And the password <password>
    When user clicked login
    Then user see the dashboard
    And user click leave management

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-U027
  Scenario Outline: Verify user can click on "Allowance Management" sidebar
    Given user on the login page
    And the email <email>
    And the password <password>
    When user clicked login
    Then user see the dashboard
    And user click allowance management

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-U028
  Scenario Outline: Verify user can click on “Appointment Management" sidebar
    Given user on the login page
    And the email <email>
    And the password <password>
    When user clicked login
    Then user see the dashboard
    And user click appointment management

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-U029
  Scenario Outline: Verify user can click on "Dashboard" sidebar
    Given user on the login page
    And the email <email>
    And the password <password>
    When user clicked login
    Then user see the dashboard
    And user click appointment management
    And user click dashboard

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-U030
  Scenario Outline: Verify user can click on the 'Profile' icon to view the dropdown.
    Given user on the login page
    And the email <email>
    And the password <password>
    When user clicked login
    Then user see the dashboard
    And user click profile icon
    And user see dropdown

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-U031
  Scenario Outline: Verify "Celebration Corner” section shows the list of the employees names and their birthday dates.
    Given user on the login page
    And the email <email>
    And the password <password>
    When user clicked login
    Then user see the dashboard
    And user see celebration corner

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-U032
  @test
  Scenario Outline: Verify user can click the 'Bell' Icon to view notifications.
    Given user on the login page
    And the email <email>
    And the password <password>
    When user clicked login
    Then user see the dashboard
    And user click bell icon
    And user see notifications

    Examples:
      | email                      | password      |
      | "awtestingbot@outlook.com" | "Perfume@123" |

  #TC-U033
  Scenario Outline: Verify user can click the "Durian" Icon to close the sidebar
    Given user on the login page
    And the email <email>
    And the password <password>
    When user clicked login
    Then user see the dashboard
    And user click company icon

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-U034
  Scenario Outline: Verify Holidays are sorted correctly in ascending order by date.
    Given user on the login page
    And the email <email>
    And the password <password>
    When user clicked login
    Then user see the dashboard
    And user see holidays displayed in ascending order by date

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-U035
  Scenario Outline: Verify Holidays are sorted correctly in ascending order by date.
    Given user on the login page
    And the email <email>
    And the password <password>
    When user clicked login
    Then user see the dashboard
    And dashboard responsive and elements realign

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-U036
  Scenario Outline: Verify user can click on "View All" link on Pending Leave section.
    Given user on the login page
    And the email <email>
    And the password <password>
    When user clicked login
    Then user see the dashboard
    And user click view all pending leave

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-U037
  Scenario Outline: Verify user can click on "View All" link on Pending claim section.
    Given user on the login page
    And the email <email>
    And the password <password>
    When user clicked login
    Then user see the dashboard
    And user click view all pending claim

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-U038
  Scenario Outline: Verify user can click on "Profile" dropdown
    Given user on the login page
    And the email <email>
    And the password <password>
    When user clicked login
    Then user see the dashboard
    And user click profile icon
    And user click profile dropdown

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-U039
  Scenario Outline: Verify user can click "Change Password" Dropdown
    Given user on the login page
    And the email <email>
    And the password <password>
    When user clicked login
    Then user see the dashboard
    And user click profile icon
    And user click change password dropdown

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-U040
  Scenario Outline: Verify dashboard still accessible if User session expired.
    Given user on the login page
    And the email <email>
    And the password <password>
    When user clicked login
    Then user see the dashboard
    And user wait before click dashboard
    And user click dashboard
    And user click dashboard
    #Then user see session timeout alert
    #And user is redirected to login page


    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-U041
  Scenario Outline: Verify "Leave Balance" section shows the list of leave summary.
    Given user on the login page
    And the email <email>
    And the password <password>
    When user clicked login
    Then user see the dashboard
    And user see list of leave summary

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-U042
  Scenario Outline: Verify "Upcoming Leave" section shows the list of upcoming leaves.
    Given user on the login page
    And the email <email>
    And the password <password>
    When user clicked login
    Then user see the dashboard
    And user see list in upcoming leave section

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |
