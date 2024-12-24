package com.epam.learn.trainerworkloadservice;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.epam.learn.trainerworkloadservice.controller.TrainerWorkloadController;
import com.epam.learn.trainerworkloadservice.model.Trainer;
import com.epam.learn.trainerworkloadservice.service.TrainerWorkloadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

public class TrainerWorkloadControllerTest {
    @Mock
    private TrainerWorkloadService workloadService;

    @InjectMocks
    private TrainerWorkloadController controller;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testGetMonthlyWorkload_ReturnsWorkload() throws Exception {
        // Arrange
        String username = "trainer123";
        int year = 2024;
        int month = 12;
        int workload = 10;

        when(workloadService.getMonthlyWorkload(username, year, month)).thenReturn(workload);

        // Act & Assert
        mockMvc.perform(get("/api/workload/{username}", username)
                        .param("year", String.valueOf(year))
                        .param("month", String.valueOf(month)))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(workload)));

        verify(workloadService, times(1)).getMonthlyWorkload(username, year, month);
    }

    @Test
    void testSearchByFirstNameAndLastName_ReturnsTrainers() throws Exception {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";

        Trainer trainer1 = new Trainer();
        trainer1.setUsername("trainer123");
        trainer1.setFirstName(firstName);
        trainer1.setLastName(lastName);

        Trainer trainer2 = new Trainer();
        trainer2.setUsername("trainer456");
        trainer2.setFirstName(firstName);
        trainer2.setLastName(lastName);

        List<Trainer> trainers = List.of(trainer1, trainer2);

        when(workloadService.getTrainersByFirstNameAndLastName(firstName, lastName)).thenReturn(trainers);

        // Act & Assert
        mockMvc.perform(get("/api/workload/{firstName}/{lastName}", firstName, lastName))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(trainers)));

        verify(workloadService, times(1)).getTrainersByFirstNameAndLastName(firstName, lastName);
    }

    @Test
    void testSearchByFirstNameAndLastName_EmptyResult() throws Exception {
        // Arrange
        String firstName = "Nonexistent";
        String lastName = "User";

        when(workloadService.getTrainersByFirstNameAndLastName(firstName, lastName)).thenReturn(List.of());

        // Act & Assert
        mockMvc.perform(get("/api/workload/{firstName}/{lastName}", firstName, lastName))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(workloadService, times(1)).getTrainersByFirstNameAndLastName(firstName, lastName);
    }
}
