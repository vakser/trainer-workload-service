package com.epam.learn.trainerworkloadservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingMonth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "month_number", nullable = false)
    private int monthNumber; // Represent months as integers (1 = January, 12 = December) Avoid using reserved keywords like "month"
    @Column(nullable = false)
    private int totalMonthlyDuration; // In hours
    @ManyToOne
    @JoinColumn(name = "year_id", nullable = false)
    private TrainingYear trainingYear;
}
