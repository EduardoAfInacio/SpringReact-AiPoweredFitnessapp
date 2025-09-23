package com.fitness.AiPowereredFitnessApp_ActivityMicroService.service;

import com.fitness.AiPowereredFitnessApp_ActivityMicroService.controller.dto.ActivityResponse;
import com.fitness.AiPowereredFitnessApp_ActivityMicroService.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;

    public ActivityResponse trackActivity(){

    }
}
