package com.fitness.AiPowereredFitnessApp_ActivityMicroService.repository;

import com.fitness.AiPowereredFitnessApp_ActivityMicroService.model.Activity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends MongoRepository<Activity, String> {
}
