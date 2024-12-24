package com.epam.learn.trainerworkloadservice.service;

import com.epam.learn.trainerworkloadservice.dao.TrainerWorkloadRepository;
import com.epam.learn.trainerworkloadservice.exception.TrainerNotFoundException;
import com.epam.learn.trainerworkloadservice.model.Trainer;
import com.epam.learn.trainerworkloadservice.dto.TrainerWorkloadRequest;
import com.epam.learn.trainerworkloadservice.model.TrainingMonth;
import com.epam.learn.trainerworkloadservice.model.TrainingYear;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainerWorkloadService {
    private final TrainerWorkloadRepository workloadRepository;

    public void processEvent(TrainerWorkloadRequest dto) {
        String username = dto.getTrainerUsername();
        int year = dto.getTrainingDate().getYear();
        int month = dto.getTrainingDate().getMonthValue();
        int trainingDuration = dto.getTrainingDuration();

        // Check if the trainer exists
        Trainer trainer = workloadRepository.findByUsername(username).orElse(null);

        if (trainer == null) {
            // Create a new Trainer record
            trainer = new Trainer();
            trainer.setUsername(username);
            trainer.setFirstName(dto.getTrainerFirstName());
            trainer.setLastName(dto.getTrainerLastName());
            trainer.setActive(dto.isActive());

            // Initialize years and months structure
            TrainingYear newYear = new TrainingYear();
            newYear.setYearNumber(year);

            TrainingMonth newMonth = new TrainingMonth();
            newMonth.setMonthNumber(month);
            newMonth.setTotalMonthlyDuration(trainingDuration);

            newYear.getMonths().add(newMonth);
            trainer.getYears().add(newYear);

            // Save the new trainer document
            workloadRepository.save(trainer);
        } else {
            // Update existing Trainer's workload
            updateTrainerWorkload(trainer, year, month, trainingDuration);
        }
    }

    private void updateTrainerWorkload(Trainer trainer, int year, int month, int trainingDuration) {
        // Find or create the year record
        TrainingYear trainingYear = trainer.getYears().stream()
                .filter(y -> y.getYearNumber() == year)
                .findFirst()
                .orElseGet(() -> {
                    TrainingYear newYear = new TrainingYear();
                    newYear.setYearNumber(year);
                    trainer.getYears().add(newYear);
                    return newYear;
                });

        // Find or create the month record
        TrainingMonth trainingMonth = trainingYear.getMonths().stream()
                .filter(m -> m.getMonthNumber() == month)
                .findFirst()
                .orElseGet(() -> {
                    TrainingMonth newMonth = new TrainingMonth();
                    newMonth.setMonthNumber(month);
                    trainingYear.getMonths().add(newMonth);
                    return newMonth;
                });

        // Update the training duration
        trainingMonth.setTotalMonthlyDuration(trainingMonth.getTotalMonthlyDuration() + trainingDuration);

        // Save the updated trainer document
        workloadRepository.save(trainer);
    }

    public int getMonthlyWorkload(String trainerUsername, int year, int month) {
        Trainer trainer = workloadRepository.findByUsername(trainerUsername)
                .orElseThrow(() -> new TrainerNotFoundException("Trainer with username " + trainerUsername + " not found"));
        return trainer.getYears().stream()
                .filter(y -> y.getYearNumber() == year)
                .flatMap(y -> y.getMonths().stream())
                .filter(m -> m.getMonthNumber() == month)
                .mapToInt(TrainingMonth::getTotalMonthlyDuration)
                .findFirst()
                .orElse(0);
    }

    public List<Trainer> getTrainersByFirstNameAndLastName(String firstName, String lastName) {
        return workloadRepository.findByFirstNameAndLastName(firstName, lastName);
    }
}
