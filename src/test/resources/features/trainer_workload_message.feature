Feature: Process trainer workload messages

  Scenario: Trainer workload microservice processes workload message
    Given a message from main microservice is received
    When the message is processed by the trainer workload microservice
    Then the trainer workload is updated in database