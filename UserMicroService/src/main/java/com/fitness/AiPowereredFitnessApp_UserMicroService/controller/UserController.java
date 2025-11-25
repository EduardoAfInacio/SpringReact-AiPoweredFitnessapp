package com.fitness.AiPowereredFitnessApp_UserMicroService.controller;

import com.fitness.AiPowereredFitnessApp_UserMicroService.controller.dto.RegisterRequest;
import com.fitness.AiPowereredFitnessApp_UserMicroService.controller.dto.UserResponse;
import com.fitness.AiPowereredFitnessApp_UserMicroService.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userKeycloakId}")
    public ResponseEntity<UserResponse> getUserProfile(@PathVariable String userKeycloakId){
        return ResponseEntity.ok(userService.getUserProfile(userKeycloakId));
    }

    @GetMapping("/{userKeycloakId}/validate")
    public ResponseEntity<Boolean> validateUser(@PathVariable String userKeycloakId){
        return ResponseEntity.ok(userService.existUserByKeycloakId(userKeycloakId));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request){
        return ResponseEntity.ok(userService.register(request));
    }
}
