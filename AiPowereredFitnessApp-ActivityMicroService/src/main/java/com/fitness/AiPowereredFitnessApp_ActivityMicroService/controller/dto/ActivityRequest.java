package com.fitness.AiPowereredFitnessApp_ActivityMicroService.controller.dto;

import com.fitness.AiPowereredFitnessApp_ActivityMicroService.model.ActivityType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ActivityRequest {
    private String userId;
    private ActivityType activityType;
    private Integer duration;
    private Integer calories;
    private LocalDateTime startTime;
    private Map<String, Object> additionalMetrics;
}
