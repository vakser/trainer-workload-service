package com.epam.learn.trainerworkloadservice.cucumber;

import com.epam.learn.trainerworkloadservice.TestMongoConfig;
import com.epam.learn.trainerworkloadservice.dao.TrainerWorkloadRepository;
import com.epam.learn.trainerworkloadservice.dto.TrainerWorkloadRequest;
import com.epam.learn.trainerworkloadservice.service.WorkloadMessageListener;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestMongoConfig.class)
public class TrainerWorkloadMessageSteps {
    @Autowired
    private WorkloadMessageListener workloadMessageListener;
    @Autowired
    private TrainerWorkloadRepository trainerWorkloadRepository;
    private static TrainerWorkloadRequest lastTrainerWorkloadRequest;

    @Given("a message from main microservice is received")
    public void aMessageFromMainMicroserviceIsReceived() {
        if (lastTrainerWorkloadRequest != null) {
            lastTrainerWorkloadRequest = workloadMessageListener.getLastMessage();
        }
    }

    @When("the message is processed by the trainer workload microservice")
    public void theMessageIsProcessedByTheTrainerWorkloadMicroservice() {
        if (lastTrainerWorkloadRequest != null) {
            workloadMessageListener.onMessage(lastTrainerWorkloadRequest);
        }
    }

    @Then("the trainer workload is updated in database")
    public void theTrainerWorkloadIsUpdatedInDatabase() {
        assertNotNull(trainerWorkloadRepository.findByUsername("John.Doe"));
    }
}
