package com.fitness.AiPowereredFitnessApp_ActivityMicroService.controller;

import com.fitness.AiPowereredFitnessApp_ActivityMicroService.controller.dto.ActivityRequest;
import com.fitness.AiPowereredFitnessApp_ActivityMicroService.controller.dto.ActivityResponse;
import com.fitness.AiPowereredFitnessApp_ActivityMicroService.service.ActivityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {
    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @PostMapping
    public ResponseEntity<ActivityResponse> saveActivity(@RequestBody ActivityRequest request){
        return ResponseEntity.ok(activityService.saveActivity(request));
    }
}
