package com.fitness.AiMicroService.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.AiMicroService.model.DTO.Activity;
import com.fitness.AiMicroService.model.Recommendation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAiService {
    private final GeminiService geminiService;

    public String generateRecommendation(Activity activity) {
        String prompt = createPromptForActivity(activity);
        String aiResponse = geminiService.getAnswer(prompt);
        Recommendation processedAiResponse = processAiResponse(activity, aiResponse);
        return aiResponse;
    }

    private Recommendation processAiResponse(Activity activity, String aiResponse) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(aiResponse);
            JsonNode textNode = jsonNode.path("candidates").get(0).path("content").path("parts").get(0).path("text");

            String text = textNode.asText()
                    .replaceAll("```json\\n","")
                    .replaceAll("\\n```", "")
                    .trim();

            log.info("AI PARSED RESPONSE: {}", text);

            JsonNode analysisNode = textNode.path("analysis");
            StringBuilder recommendation = new StringBuilder();
            addAnalysisSection(recommendation, analysisNode,"overall", "Overall:");
            addAnalysisSection(recommendation, analysisNode,"pace", "Pace:");
            addAnalysisSection(recommendation, analysisNode,"heartRate", "Heart Rate:");
            addAnalysisSection(recommendation, analysisNode,"caloriesBurned", "Calories:");

            List<String> improvements = extractImprovements(textNode.path("improvements"));
            List<String> suggestions = extractSuggestions(textNode.path("suggestions"));
            List<String> safety = extractSafetyGuidelines(textNode.path("safety"));

            return Recommendation.builder()
                    .userId(activity.getUserId())
                    .activityId(activity.getId())
                    .activityType(activity.getType().name())
                    .recommendation(recommendation.toString().trim())
                    .improvements(improvements)
                    .suggestions(suggestions)
                    .safety(safety)
                    .build();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void addAnalysisSection(StringBuilder fullAnalysis, JsonNode analysisNode, String key, String prefix) {
        if(!analysisNode.path(key).isMissingNode()){
            fullAnalysis.append(prefix).append(analysisNode.path(key).asText()).append("\n\n");
        }
    }

    private List<String> extractImprovements(JsonNode improvementsNode){
        List<String> improvements = new ArrayList<>();
        if(improvementsNode.isArray()){
            improvementsNode.forEach(improvement -> {
                String area = improvement.path("area").asText();
                String recommendation = improvement.path("recommendation").asText();
                improvements.add(String.format("%s:, %s", area, recommendation));
            });
        }
        return improvements.isEmpty() ?
                Collections.singletonList("No specific improvements provided") :
                improvements;
    }

    private List<String> extractSuggestions(JsonNode suggestionsNode){
        List<String> suggestions = new ArrayList<>();
        if(suggestionsNode.isArray()){
            suggestionsNode.forEach(suggestion -> {
                String workout = suggestion.path("workout").asText();
                String description = suggestion.path("description").asText();
                suggestions.add(String.format("%s:, %s", workout, description));
            });
        }
        return suggestions.isEmpty() ?
                Collections.singletonList("No specific suggestions provided") :
                suggestions;
    }

    private List<String> extractSafetyGuidelines(JsonNode safetyNode){
        List<String> safetyGuidelines = new ArrayList<>();
        if(safetyNode.isArray()){
            safetyNode.forEach(safetyGuideline -> {
                safetyGuidelines.add(safetyGuideline.asText());
            });
        }
        return safetyGuidelines.isEmpty() ?
                Collections.singletonList("Follow general safety guidelines") :
                safetyGuidelines;
    }

    private String createPromptForActivity(Activity activity) {
        return String.format("""
        Analyze this fitness activity and provide detailed recommendations in the following EXACT JSON format:
        {
          "analysis": {
            "overall": "Overall analysis here",
            "pace": "Pace analysis here",
            "heartRate": "Heart rate analysis here",
            "caloriesBurned": "Calories analysis here"
          },
          "improvements": [
            {
              "area": "Area name",
              "recommendation": "Detailed recommendation"
            }
          ],
          "suggestions": [
            {
              "workout": "Workout name",
              "description": "Detailed workout description"
            }
          ],
          "safety": [
            "Safety point 1",
            "Safety point 2"
          ]
        }

        Analyze this activity:
        Activity Type: %s
        Duration: %d minutes
        Calories Burned: %d
        Additional Metrics: %s
        
        Provide detailed analysis focusing on performance, improvements, next workout suggestions, and safety guidelines.
        Ensure the response follows the EXACT JSON format shown above.
        """,
                activity.getType(),
                activity.getDuration(),
                activity.getCaloriesBurned(),
                activity.getAdditionalMetrics()
        );
    }
}
