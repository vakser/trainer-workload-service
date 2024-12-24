package com.epam.learn.trainerworkloadservice.service;

import com.epam.learn.trainerworkloadservice.configuration.ActiveMQConfig;
import com.epam.learn.trainerworkloadservice.dto.TrainerWorkloadRequest;
import com.epam.learn.trainerworkloadservice.exception.InvalidMessageException;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkloadMessageListener {
    private final TrainerWorkloadService trainerWorkloadService;
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = ActiveMQConfig.WORKLOAD_QUEUE)
    public void onMessage(TrainerWorkloadRequest workloadRequest) {
        try {
            validateMessage(workloadRequest);
            trainerWorkloadService.processEvent(workloadRequest);
        } catch (InvalidMessageException e) {
            // Log the invalid message and route to DLQ
            System.err.println("Invalid message detected: " + workloadRequest);
            sendToDeadLetterQueue(workloadRequest, e.getMessage());
        } catch (Exception e) {
            // Handle other exceptions (e.g., processing errors)
            System.err.println("Error processing message: " + e.getMessage());
            throw e; // Allow retry mechanism to handle it
        }
    }

    private void validateMessage(TrainerWorkloadRequest request) throws InvalidMessageException {
        if (request.getTrainerUsername() == null || request.getTrainerUsername().isEmpty()) {
            throw new InvalidMessageException("Trainer username is missing");
        }
        if (request.getTrainingDate() == null) {
            throw new InvalidMessageException("Training date is missing");
        }
        if (request.getTrainingDuration() == null || request.getTrainingDuration() <= 0) {
            throw new InvalidMessageException("Invalid training duration");
        }
        // Add more validation if needed
    }

    private void sendToDeadLetterQueue(TrainerWorkloadRequest workloadRequest, String reason) {
        try {
            jmsTemplate.convertAndSend(ActiveMQConfig.DEAD_LETTER_QUEUE, workloadRequest, message -> {
                message.setStringProperty("FailureReason", reason);
                return message;
            });
            System.out.println("Invalid message sent to DLQ with reason: " + reason);
        } catch (Exception e) {
            System.err.println("Failed to send message to DLQ: " + e.getMessage());
        }
    }

}
