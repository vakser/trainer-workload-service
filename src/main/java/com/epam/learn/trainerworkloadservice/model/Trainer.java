package com.epam.learn.trainerworkloadservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Document(collection = "trainers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@CompoundIndex(name = "firstName_lastName_idx", def = "{'firstName': 1, 'lastName': 1}")
public class Trainer {
    @Id
    private ObjectId _id;
    private String username;
    private String firstName;
    private String lastName;
    private boolean isActive;
    private List<TrainingYear> years = new ArrayList<>();
}
