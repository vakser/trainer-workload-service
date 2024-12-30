package com.epam.learn.trainerworkloadservice.dao;

import com.epam.learn.trainerworkloadservice.model.Trainer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerWorkloadRepository extends MongoRepository<Trainer, String> {
    Optional<Trainer> findByUsername(String username);

    List<Trainer> findByFirstNameAndLastName(String firstName, String lastName);
}
