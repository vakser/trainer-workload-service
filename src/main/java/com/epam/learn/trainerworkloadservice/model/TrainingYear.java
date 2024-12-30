package com.epam.learn.trainerworkloadservice.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingYear {
    private int yearNumber;
    private List<TrainingMonth> months = new ArrayList<>();
}
