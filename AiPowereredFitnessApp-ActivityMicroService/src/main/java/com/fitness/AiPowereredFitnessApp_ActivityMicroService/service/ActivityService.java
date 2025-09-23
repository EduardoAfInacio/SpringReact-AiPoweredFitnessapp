package com.fitness.AiPowereredFitnessApp_ActivityMicroService.service;

import com.fitness.AiPowereredFitnessApp_ActivityMicroService.controller.dto.ActivityRequest;
import com.fitness.AiPowereredFitnessApp_ActivityMicroService.controller.dto.ActivityResponse;
import com.fitness.AiPowereredFitnessApp_ActivityMicroService.mapper.ActivityMapper;
import com.fitness.AiPowereredFitnessApp_ActivityMicroService.model.Activity;
import com.fitness.AiPowereredFitnessApp_ActivityMicroService.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;

    public ActivityResponse saveActivity(ActivityRequest request){
        var activity = Activity.builder()
                .userId(request.getUserId())
                .type(request.getActivityType())
                .duration(request.getDuration())
                .caloriesBurned(request.getCalories())
                .startTime(request.getStartTime())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();

        var activityEntity = activityRepository.save(activity);
        return activityMapper.toResponseFromEntity(activityEntity);
    }
}
