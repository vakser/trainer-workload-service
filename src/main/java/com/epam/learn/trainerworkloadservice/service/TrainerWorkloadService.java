package com.epam.learn.trainerworkloadservice.service;

import com.epam.learn.trainerworkloadservice.dao.TrainerWorkloadRepository;
import com.epam.learn.trainerworkloadservice.exception.TrainerNotFoundException;
import com.epam.learn.trainerworkloadservice.model.Trainer;
import com.epam.learn.trainerworkloadservice.dto.TrainerWorkloadRequest;
import com.epam.learn.trainerworkloadservice.model.TrainingMonth;
import com.epam.learn.trainerworkloadservice.model.TrainingYear;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TrainerWorkloadService {
    private final TrainerWorkloadRepository workloadRepository;

    @Transactional
    public void updateWorkload(TrainerWorkloadRequest dto) {
        Trainer trainer = workloadRepository.findById(dto.getTrainerUsername())
                .orElseGet(() -> {
                    Trainer newTrainer = new Trainer();
                    newTrainer.setUsername(dto.getTrainerUsername());
                    newTrainer.setFirstName(dto.getTrainerFirstName());
                    newTrainer.setLastName(dto.getTrainerLastName());
                    newTrainer.setActive(dto.isActive());
                    return workloadRepository.save(newTrainer);
                });

        if (!trainer.isActive()) {
            throw new IllegalArgumentException("Trainer is inactive");
        }

        LocalDate trainingDate = dto.getTrainingDate();
        int year = trainingDate.getYear();
        int month = trainingDate.getMonthValue();

        TrainingYear trainingYear = trainer.getYears().stream()
                .filter(y -> y.getYearNumber() == year)
                .findFirst()
                .orElseGet(() -> {
                    TrainingYear newTrainingYear = new TrainingYear();
                    newTrainingYear.setYearNumber(year);
                    newTrainingYear.setTrainer(trainer);
                    trainer.getYears().add(newTrainingYear);
                    return newTrainingYear;
                });

        TrainingMonth trainingMonth = trainingYear.getMonths().stream()
                .filter(m -> m.getMonthNumber() == month)
                .findFirst()
                .orElseGet(() -> {
                    TrainingMonth newTrainingMonth = new TrainingMonth();
                    newTrainingMonth.setMonthNumber(month);
                    newTrainingMonth.setTrainingYear(trainingYear);
                    trainingYear.getMonths().add(newTrainingMonth);
                    return newTrainingMonth;
                });

        if ("ADD".equalsIgnoreCase(dto.getActionType())) {
            trainingMonth.setTotalMonthlyDuration(trainingMonth.getTotalMonthlyDuration() + dto.getTrainingDuration());
        } else if ("DELETE".equalsIgnoreCase(dto.getActionType())) {
            trainingMonth.setTotalMonthlyDuration(Math.max(0, trainingMonth.getTotalMonthlyDuration() - dto.getTrainingDuration()));
        }

        workloadRepository.save(trainer);
    }

    public int getMonthlyWorkload(String trainerUsername, int year, int month) {
        Trainer trainer = workloadRepository.findById(trainerUsername)
                .orElseThrow(() -> new TrainerNotFoundException("Trainer with username " + trainerUsername + " not found"));
        return trainer.getYears().stream()
                .filter(y -> y.getYearNumber() == year)
                .flatMap(y -> y.getMonths().stream())
                .filter(m -> m.getMonthNumber() == month)
                .mapToInt(TrainingMonth::getTotalMonthlyDuration)
                .findFirst()
                .orElse(0);
    }
}
