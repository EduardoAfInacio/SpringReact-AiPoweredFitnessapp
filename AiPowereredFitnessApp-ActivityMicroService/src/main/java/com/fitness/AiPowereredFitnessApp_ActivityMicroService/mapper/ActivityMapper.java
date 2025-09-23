package com.fitness.AiPowereredFitnessApp_ActivityMicroService.mapper;

import com.fitness.AiPowereredFitnessApp_ActivityMicroService.controller.dto.ActivityResponse;
import com.fitness.AiPowereredFitnessApp_ActivityMicroService.model.Activity;
import org.springframework.stereotype.Component;

@Component
public class ActivityMapper {
    public ActivityResponse toResponseFromEntity(Activity activity){
        return new ActivityResponse(
                activity.getId(),
                activity.getUserId(),
                activity.getType(),
                activity.getDuration(),
                activity.getCaloriesBurned(),
                activity.getStartTime(),
                activity.getAdditionalMetrics(),
                activity.getCreatedAt(),
                activity.getUpdatedAt()
        );
    }
}
