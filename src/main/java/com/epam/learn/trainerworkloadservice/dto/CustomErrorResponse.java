package com.epam.learn.trainerworkloadservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomErrorResponse {
    private HttpStatus status;
    private String error;
    private String message;
}
