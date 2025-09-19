package com.fitness.AiPowereredFitnessApp_UserMicroService.service;

import com.fitness.AiPowereredFitnessApp_UserMicroService.controller.dto.RegisterRequest;
import com.fitness.AiPowereredFitnessApp_UserMicroService.controller.dto.UserResponse;
import com.fitness.AiPowereredFitnessApp_UserMicroService.mapper.UserMapper;
import com.fitness.AiPowereredFitnessApp_UserMicroService.model.User;
import com.fitness.AiPowereredFitnessApp_UserMicroService.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserResponse register(RegisterRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        var userSaved = userRepository.save(user);
        return userMapper.toUserResponseFromEntityAllArgs(userSaved);
    }

    public UserResponse getUserProfile(String userId) {
        var userFound = userRepository.findById(userId);
        if(userFound.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return userMapper.toUserResponseFromEntityAllArgs(userFound.get());
    }
}
