package com.epam.learn.trainerworkloadservice.controller;

import com.epam.learn.trainerworkloadservice.dto.TrainerWorkloadRequest;
import com.epam.learn.trainerworkloadservice.service.TrainerWorkloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workload")
@RequiredArgsConstructor
public class TrainerWorkloadController {
    private final TrainerWorkloadService workloadService;

    @PostMapping("/update")
    public ResponseEntity<String> updateTrainerWorkload(@RequestBody TrainerWorkloadRequest request) {
        workloadService.updateWorkload(request);
        return ResponseEntity.ok("Workload updated successfully");
    }

    @GetMapping("/{username}")
    public ResponseEntity<Integer> getMonthlyWorkload(@PathVariable String username,
                                                      @RequestParam int year,
                                                      @RequestParam int month) {
        int workload = workloadService.getMonthlyWorkload(username, year, month);
        return ResponseEntity.ok(workload);
    }
}
