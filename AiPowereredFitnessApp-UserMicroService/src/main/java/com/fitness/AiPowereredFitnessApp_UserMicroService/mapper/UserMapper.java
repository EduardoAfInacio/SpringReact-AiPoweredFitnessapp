package com.fitness.AiPowereredFitnessApp_UserMicroService.mapper;

import com.fitness.AiPowereredFitnessApp_UserMicroService.controller.dto.UserResponse;
import com.fitness.AiPowereredFitnessApp_UserMicroService.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toUserResponseFromEntityAllArgs(User user){
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
