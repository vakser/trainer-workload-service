package com.epam.learn.trainerworkloadservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingYear {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "year_number", nullable = false)
    private int yearNumber; // Avoid using reserved keywords like "year"
    @ManyToOne
    @JoinColumn(name = "trainer_username", nullable = false)
    private Trainer trainer;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "trainingYear", orphanRemoval = true)
    private List<TrainingMonth> months = new ArrayList<>();
}
