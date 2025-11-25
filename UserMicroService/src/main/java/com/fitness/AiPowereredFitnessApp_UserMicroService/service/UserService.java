package com.fitness.AiPowereredFitnessApp_UserMicroService.service;

import com.fitness.AiPowereredFitnessApp_UserMicroService.controller.dto.RegisterRequest;
import com.fitness.AiPowereredFitnessApp_UserMicroService.controller.dto.UserResponse;
import com.fitness.AiPowereredFitnessApp_UserMicroService.mapper.UserMapper;
import com.fitness.AiPowereredFitnessApp_UserMicroService.model.User;
import com.fitness.AiPowereredFitnessApp_UserMicroService.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserResponse register(RegisterRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            User existingUser = userRepository.findByEmail(request.getEmail());
            return userMapper.toUserResponseFromEntityAllArgs(existingUser);
        }
        User user = new User();
        user.setKeycloakId(request.getKeycloakId());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        var userSaved = userRepository.save(user);
        return userMapper.toUserResponseFromEntityAllArgs(userSaved);
    }

    public UserResponse getUserProfile(String userKeycloakId) {
        var userFound = userRepository.findByKeycloakId(userKeycloakId);
        if(userFound.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return userMapper.toUserResponseFromEntityAllArgs(userFound.get());
    }

    public Boolean existUserByKeycloakId(String userKeycloakId) {
        log.info("Calling validation API endpoint");
        return userRepository.existsByKeycloakId(userKeycloakId);
    }
}
