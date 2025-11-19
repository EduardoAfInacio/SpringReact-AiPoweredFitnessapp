package com.fitness.AiPowereredFitnessApp_UserMicroService.repository;

import com.fitness.AiPowereredFitnessApp_UserMicroService.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);

    Boolean existsByKeycloakId(String keycloakId);

    User findByEmail(String email);
}
