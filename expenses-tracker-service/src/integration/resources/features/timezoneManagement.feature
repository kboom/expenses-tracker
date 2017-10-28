Feature: Timezone management feature

  Scenario: User can create a timezone
    Given there is a user 'Stefan'
    And that user wants to create a new timezone
    And this timezone has name set to 'Home'
    And this timezone has city set to 'Krakow'
    And this timezone has time difference set to '2'
    When 'Stefan' creates this timezone
    Then this timezone is saved