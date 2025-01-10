package com.epam.learn.trainerworkloadservice.cucumber;

import com.epam.learn.trainerworkloadservice.dao.TrainerWorkloadRepository;
import com.epam.learn.trainerworkloadservice.dto.TrainerWorkloadRequest;
import com.epam.learn.trainerworkloadservice.exception.TrainerNotFoundException;
import com.epam.learn.trainerworkloadservice.model.Trainer;
import com.epam.learn.trainerworkloadservice.model.TrainingMonth;
import com.epam.learn.trainerworkloadservice.model.TrainingYear;
import com.epam.learn.trainerworkloadservice.service.TrainerWorkloadService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@CucumberContextConfiguration
public class TrainerWorkloadServiceStepDefinitions {
    @Autowired
    private TrainerWorkloadService trainerWorkloadService;

    @MockBean
    private TrainerWorkloadRepository workloadRepository;

    private TrainerWorkloadRequest request;
    private Exception exception;

    @Given("a new trainer workload request with username {string}, year {int}, month {int}, duration {int}")
    public void givenNewTrainerRequest(String username, int year, int month, int duration) {
        request = new TrainerWorkloadRequest();
        request.setTrainerUsername(username);
        request.setTrainingDate(LocalDate.of(year, month, 1));
        request.setTrainingDuration(duration);
    }

    @When("the processEvent API is called")
    public void whenProcessEventCalled() {
        try {
            trainerWorkloadService.processEvent(request);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("a new trainer record should be created with username {string}")
    public void thenNewTrainerCreated(String username) {
        Mockito.verify(workloadRepository, Mockito.times(1)).save(Mockito.argThat(trainer -> trainer.getUsername().equals(username)));
    }

    @Given("no trainer exists with username {string}")
    public void givenNoTrainerExists(String username) {
        Mockito.when(workloadRepository.findByUsername(username)).thenReturn(Optional.empty());
    }

    @When("the getMonthlyWorkload API is called for username {string}, year {int}, and month {int}")
    public void whenGetMonthlyWorkloadCalled(String username, int year, int month) {
        try {
            trainerWorkloadService.getMonthlyWorkload(username, year, month);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("a TrainerNotFoundException should be thrown")
    public void thenTrainerNotFoundExceptionThrown() {
        Assertions.assertInstanceOf(TrainerNotFoundException.class, exception);
    }

    @Given("an existing trainer with username {string}, training year {int}, training month {int} and training duration {int}")
    public void anExistingTrainerWithUsernameTrainingYearTrainingMonthAndTrainingDuration(String trainerUsername,
                                                                                          int year, int month, int duration) {
        Trainer trainer = new Trainer();
        trainer.setUsername(trainerUsername);
        TrainingMonth trainingMonth = new TrainingMonth();
        trainingMonth.setMonthNumber(month);
        trainingMonth.setTotalMonthlyDuration(duration);
        TrainingYear trainingYear = new TrainingYear();
        trainingYear.setYearNumber(year);
        trainingYear.setMonths(List.of(trainingMonth));
        trainer.setYears(List.of(trainingYear));
        Mockito.when(workloadRepository.findByUsername(trainerUsername)).thenReturn(Optional.of(trainer));
    }


    @Then("a trainer record with username {string}, training year {int}, training month {int}, training duration1 {int} to add, so total duration should be updated to training duration2 {int}")
    public void aTrainerRecordWithUsernameTrainingYearTrainingMonthTrainingDurationShouldBeUpdatedTo(String username, int year, int month, int duration1, int duration2) {
        Trainer trainer = workloadRepository.findByUsername(username).get();
        request = new TrainerWorkloadRequest();
        request.setTrainerUsername(username);
        request.setTrainingDate(LocalDate.of(year, month, 1));
        request.setTrainingDuration(duration1);
        workloadRepository.save(trainer);
        Assertions.assertEquals(duration2, trainer.getYears().get(0).getMonths().get(0).getTotalMonthlyDuration());
    }
}
