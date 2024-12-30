package com.epam.learn.trainerworkloadservice;

import com.epam.learn.trainerworkloadservice.dao.TrainerWorkloadRepository;
import com.epam.learn.trainerworkloadservice.dto.TrainerWorkloadRequest;
import com.epam.learn.trainerworkloadservice.exception.TrainerNotFoundException;
import com.epam.learn.trainerworkloadservice.model.Trainer;
import com.epam.learn.trainerworkloadservice.model.TrainingMonth;
import com.epam.learn.trainerworkloadservice.model.TrainingYear;
import com.epam.learn.trainerworkloadservice.service.TrainerWorkloadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TrainerWorkloadServiceTest {
    @Mock
    private TrainerWorkloadRepository workloadRepository;

    @InjectMocks
    private TrainerWorkloadService workloadService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void processEvent_shouldCreateNewTrainerIfNotExist() {
        // Arrange
        TrainerWorkloadRequest request = new TrainerWorkloadRequest();
        request.setTrainerUsername("john_doe");
        request.setTrainerFirstName("John");
        request.setTrainerLastName("Doe");
        request.setActive(true);
        request.setTrainingDate(LocalDate.of(2023, 12, 1));
        request.setTrainingDuration(5);

        when(workloadRepository.findByUsername("john_doe")).thenReturn(Optional.empty());

        // Act
        workloadService.processEvent(request);

        // Assert
        ArgumentCaptor<Trainer> captor = ArgumentCaptor.forClass(Trainer.class);
        verify(workloadRepository).save(captor.capture());

        Trainer savedTrainer = captor.getValue();
        assertEquals("john_doe", savedTrainer.getUsername());
        assertEquals(1, savedTrainer.getYears().size());
        assertEquals(12, savedTrainer.getYears().get(0).getMonths().get(0).getMonthNumber());
        assertEquals(5, savedTrainer.getYears().get(0).getMonths().get(0).getTotalMonthlyDuration());
    }

    @Test
    void processEvent_shouldUpdateExistingTrainerWorkload() {
        // Arrange
        Trainer trainer = new Trainer();
        trainer.setUsername("john_doe");
        trainer.setYears(new ArrayList<>());

        TrainingYear year = new TrainingYear();
        year.setYearNumber(2023);
        year.setMonths(new ArrayList<>());

        TrainingMonth month = new TrainingMonth();
        month.setMonthNumber(12);
        month.setTotalMonthlyDuration(10);

        year.getMonths().add(month);
        trainer.getYears().add(year);

        when(workloadRepository.findByUsername("john_doe")).thenReturn(Optional.of(trainer));

        TrainerWorkloadRequest request = new TrainerWorkloadRequest();
        request.setTrainerUsername("john_doe");
        request.setTrainerFirstName("John");
        request.setTrainerLastName("Doe");
        request.setActive(true);
        request.setTrainingDate(LocalDate.of(2023, 12, 1));
        request.setTrainingDuration(5);

        // Act
        workloadService.processEvent(request);

        // Assert
        ArgumentCaptor<Trainer> captor = ArgumentCaptor.forClass(Trainer.class);
        verify(workloadRepository).save(captor.capture());

        Trainer updatedTrainer = captor.getValue();
        assertEquals(15, updatedTrainer.getYears().get(0).getMonths().get(0).getTotalMonthlyDuration());
    }

    @Test
    void getMonthlyWorkload_shouldReturnCorrectWorkload() {
        // Arrange
        Trainer trainer = new Trainer();
        trainer.setUsername("john_doe");

        TrainingYear year = new TrainingYear();
        year.setYearNumber(2023);
        year.setMonths(new ArrayList<>());

        TrainingMonth month = new TrainingMonth();
        month.setMonthNumber(12);
        month.setTotalMonthlyDuration(10);

        year.getMonths().add(month);
        trainer.getYears().add(year);

        when(workloadRepository.findByUsername("john_doe")).thenReturn(Optional.of(trainer));

        // Act
        int workload = workloadService.getMonthlyWorkload("john_doe", 2023, 12);

        // Assert
        assertEquals(10, workload);
    }

    @Test
    void getMonthlyWorkload_shouldThrowExceptionIfTrainerNotFound() {
        // Arrange
        when(workloadRepository.findByUsername("john_doe")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TrainerNotFoundException.class, () ->
                workloadService.getMonthlyWorkload("john_doe", 2023, 12));
    }

    @Test
    void getTrainersByFirstNameAndLastName_shouldReturnMatchingTrainers() {
        // Arrange
        Trainer trainer1 = new Trainer();
        trainer1.setUsername("john_doe");
        trainer1.setFirstName("John");
        trainer1.setLastName("Doe");

        Trainer trainer2 = new Trainer();
        trainer2.setUsername("jane_doe");
        trainer2.setFirstName("John");
        trainer2.setLastName("Doe");

        List<Trainer> trainers = List.of(trainer1, trainer2);
        when(workloadRepository.findByFirstNameAndLastName("John", "Doe")).thenReturn(trainers);

        // Act
        List<Trainer> result = workloadService.getTrainersByFirstNameAndLastName("John", "Doe");

        // Assert
        assertEquals(2, result.size());
        assertEquals("john_doe", result.get(0).getUsername());
        assertEquals("jane_doe", result.get(1).getUsername());
    }
}
