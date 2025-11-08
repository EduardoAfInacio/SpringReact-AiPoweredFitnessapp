package com.fitness.AiPowereredFitnessApp_ActivityMicroService.service;

import com.fitness.AiPowereredFitnessApp_ActivityMicroService.controller.dto.ActivityRequest;
import com.fitness.AiPowereredFitnessApp_ActivityMicroService.controller.dto.ActivityResponse;
import com.fitness.AiPowereredFitnessApp_ActivityMicroService.mapper.ActivityMapper;
import com.fitness.AiPowereredFitnessApp_ActivityMicroService.model.Activity;
import com.fitness.AiPowereredFitnessApp_ActivityMicroService.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;
    private final UserServiceValidation userServiceValidation;
    private final RabbitTemplate rabbitTemplate;
    @Value("${rabbitmq.exchange.name}")
    private String exchange;
    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public ActivityResponse saveActivity(ActivityRequest request){

        boolean isValidUser = userServiceValidation.validateUserId(request.getUserId());

        if(!isValidUser){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
        }

        var activity = Activity.builder()
                .userId(request.getUserId())
                .type(request.getActivityType())
                .duration(request.getDuration())
                .caloriesBurned(request.getCalories())
                .startTime(request.getStartTime())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();

        var activityEntity = activityRepository.save(activity);

        try{
            rabbitTemplate.convertAndSend(exchange, routingKey, activityEntity);
        }catch (Exception e){
            log.error("Error sending message to rabbitmq: {}", e.getMessage());
        }

        return activityMapper.toResponseFromEntity(activityEntity);
    }

    public List<ActivityResponse> getUserActivities(String userId) {
        List<Activity> activities = activityRepository.findByUserId(userId);
        return activities.stream().map(activityMapper::toResponseFromEntity).toList();
    }


    public ActivityResponse getActivityById(String activityId) {
        var activity = activityRepository.findById(activityId);
        if(activity.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Activity not found");
        }

        return activityMapper.toResponseFromEntity(activity.get());
    }
}
