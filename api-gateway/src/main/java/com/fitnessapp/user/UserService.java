package com.fitnessapp.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final WebClient webClient;

    public boolean validateUserId(String userKeycloakId) {
        log.info("Checking if user exists with id: {}", userKeycloakId);
        try{
            return webClient.get()
                    .uri("/api/users/{userKeycloakId}/validate")
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
        }catch (WebClientResponseException e){
            if(e.getStatusCode() == HttpStatus.NOT_FOUND){
                throw new RuntimeException("User not found:" + userKeycloakId);
            }else if(e.getStatusCode() == HttpStatus.BAD_REQUEST){
                throw new RuntimeException("Invalid request:" + userKeycloakId);
            }
        }
        return false;
    }
}
