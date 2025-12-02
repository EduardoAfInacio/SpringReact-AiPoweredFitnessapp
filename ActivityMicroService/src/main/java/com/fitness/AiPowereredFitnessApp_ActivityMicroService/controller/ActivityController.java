package com.fitness.AiPowereredFitnessApp_ActivityMicroService.controller;

import com.fitness.AiPowereredFitnessApp_ActivityMicroService.controller.dto.ActivityRequest;
import com.fitness.AiPowereredFitnessApp_ActivityMicroService.controller.dto.ActivityResponse;
import com.fitness.AiPowereredFitnessApp_ActivityMicroService.service.ActivityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {
    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @PostMapping
    public ResponseEntity<ActivityResponse> saveActivity(@RequestBody ActivityRequest request, @RequestHeader("X-User-ID") String userId){
        if(userId != null){
            request.setUserId(userId);
        }
        return ResponseEntity.ok(activityService.saveActivity(request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ActivityResponse>> getUserActivities(@PathVariable String userId){
        return ResponseEntity.ok(activityService.getUserActivities(userId));
    }

    @GetMapping("/{activityId}")
    public ResponseEntity<ActivityResponse> getActivityById(@PathVariable String activityId){
        return ResponseEntity.ok(activityService.getActivityById(activityId));
    }
}
