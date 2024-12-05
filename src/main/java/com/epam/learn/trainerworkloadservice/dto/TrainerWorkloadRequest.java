package com.epam.learn.trainerworkloadservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerWorkloadRequest {
    @NotBlank
    private String trainerUsername;
    @NotBlank
    private String trainerFirstName;
    @NotBlank
    private String trainerLastName;
    private boolean isActive;
    @NotNull
    private LocalDate trainingDate;
    @NotNull
    private Integer trainingDuration;
    @NotBlank
    private String actionType; // ADD or DELETE
}
