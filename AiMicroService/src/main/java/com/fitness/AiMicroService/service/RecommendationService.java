package com.fitness.AiMicroService.service;

import com.fitness.AiMicroService.model.Recommendation;
import com.fitness.AiMicroService.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final RecommendationRepository recommendationRepository;

    public List<Recommendation> getUserRecommendations(String userId) {
        return recommendationRepository.findAllByUserId(userId);
    }

    public Recommendation getActivityRecommendation(String activityId) {
        return recommendationRepository.findByActivityId(activityId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No recommendation found for this activity: " + activityId)
        );
    }
}
