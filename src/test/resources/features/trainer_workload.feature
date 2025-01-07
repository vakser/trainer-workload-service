Feature: Trainer Workload Management

  Scenario: Create a new trainer workload record
    Given a new trainer workload request with username "john_doe", year 2023, month 12, duration 120
    And no trainer exists with username "john_doe"
    When the processEvent API is called
    Then a new trainer record should be created with username "john_doe"

  Scenario: Update an existing trainer's workload
    Given an existing trainer with username "john_doe", training year 2023, training month 12 and training duration 120
    And a new trainer workload request with username "john_doe", year 2023, month 12, duration 80
    When the processEvent API is called
    Then a trainer record with username "john_doe", training year 2023, training month 12, training duration1 80 to add, so total duration should be updated to training duration2 200

  Scenario: Throw exception when fetching workload for non-existent trainer
    Given no trainer exists with username "non_existent_trainer"
    When the getMonthlyWorkload API is called for username "non_existent_trainer", year 2023, and month 12
    Then a TrainerNotFoundException should be thrown
