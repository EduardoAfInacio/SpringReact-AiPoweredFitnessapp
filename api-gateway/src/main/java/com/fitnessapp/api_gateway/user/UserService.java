package com.fitnessapp.api_gateway.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final WebClient webClient;

    public Mono<Boolean> validateUserId(String userKeycloakId) {
        log.info("Checking if user exists with id: {}", userKeycloakId);
        return webClient.get()
                .uri("/api/users/{userKeycloakId}/validate", userKeycloakId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    if(ex.getStatusCode() == HttpStatus.NOT_FOUND){
                        return Mono.error(new RuntimeException("User not found" + userKeycloakId));
                    }else if(ex.getStatusCode() == HttpStatus.BAD_REQUEST){
                        return Mono.error(new RuntimeException("Invalid request" + userKeycloakId));
                    }return Mono.error(new RuntimeException("Unexpected error" + ex.getMessage()));
                });
    }

    public Mono<UserResponse> registerUser(RegisterRequest request) {
        log.info("Registering user: {}", request);
        return webClient.post()
                .uri("/api/users/register")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    if(e.getStatusCode() == HttpStatus.BAD_REQUEST)
                        return Mono.error(new RuntimeException("Bad request" + e.getMessage()));
                    else if(e.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR)
                        return Mono.error(new RuntimeException("Internal server error" + e.getMessage()));
                    return Mono.error(new RuntimeException("Unexpected error" + e.getMessage()));
                });
    }
}
