package com.epam.learn.trainerworkloadservice.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingMonth {
    private int monthNumber; // Represent months as integers (1 = January, 12 = December)
    private int totalMonthlyDuration; // In hours
}
