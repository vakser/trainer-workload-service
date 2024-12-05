package com.epam.learn.trainerworkloadservice.dao;

import com.epam.learn.trainerworkloadservice.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerWorkloadRepository extends JpaRepository<Trainer, String> {
    Trainer findByUsername(String username);
}
