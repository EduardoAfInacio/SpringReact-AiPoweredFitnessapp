package com.fitness.AiMicroService.service;

import com.fitness.AiMicroService.model.DTO.Activity;
import com.fitness.AiMicroService.model.Recommendation;
import com.fitness.AiMicroService.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListener {
    private static final String queueName = "activity.queue";
    private final ActivityAiService activityAiService;
    private final RecommendationRepository recommendationRepository;

    @RabbitListener(queues = queueName)
    public void activityMessageListener(Activity activity) {
        log.info("Message received: {}", activity.toString());
        Recommendation processedRecommendation = activityAiService.generateRecommendation(activity);
        recommendationRepository.save(processedRecommendation);
    }
}
