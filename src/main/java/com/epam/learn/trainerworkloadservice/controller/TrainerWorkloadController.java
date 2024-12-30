package com.epam.learn.trainerworkloadservice.controller;

import com.epam.learn.trainerworkloadservice.model.Trainer;
import com.epam.learn.trainerworkloadservice.service.TrainerWorkloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workload")
@RequiredArgsConstructor
public class TrainerWorkloadController {
    private final TrainerWorkloadService workloadService;

    @GetMapping("/{username}")
    public ResponseEntity<Integer> getMonthlyWorkload(@PathVariable String username,
                                                      @RequestParam int year,
                                                      @RequestParam int month) {
        int workload = workloadService.getMonthlyWorkload(username, year, month);
        return ResponseEntity.ok(workload);
    }

    @GetMapping("/{firstName}/{lastName}")
    public ResponseEntity<List<Trainer>> searchByFirstNameAndLastName(@PathVariable String firstName,
                                                                      @PathVariable String lastName) {
        List<Trainer> trainers = workloadService.getTrainersByFirstNameAndLastName(firstName, lastName);
        return ResponseEntity.ok(trainers);
    }
}
