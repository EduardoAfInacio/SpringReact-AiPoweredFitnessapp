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

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserProfile(@PathVariable String userId){
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    @GetMapping("/{userId}/validate")
    public ResponseEntity<Boolean> validateUser(@PathVariable String userId){
        return ResponseEntity.ok(userService.existUserById(userId));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request){
        return ResponseEntity.ok(userService.register(request));
    }
}
