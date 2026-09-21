package com.project.project.user.service;

import com.project.project.common.exception.ApiException;
import com.project.project.user.dto.CreateUserRequest;
import com.project.project.user.dto.UpdateUserRequest;
import com.project.project.user.dto.UserResponse;
import com.project.project.user.entity.User;
import com.project.project.user.mapper.UserMapper;
import com.project.project.user.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserResponse createUser(CreateUserRequest createUserRequest) {
        User user = UserMapper.toEntity(createUserRequest);

        user.setName(createUserRequest.name().trim());

        String email = createUserRequest.email().trim().toLowerCase();
        if (existsByEmail(email)) throw new ApiException(HttpStatus.CONFLICT, "Email already exists");
        user.setEmail(email);

        user.setPassword(createUserRequest.password()); // need to hash the password later when auth is made

        User savedUser = userRepository.save(user);
        return UserMapper.toResponse(savedUser);
    }

    public UserResponse updateUser(Long id, UpdateUserRequest update) {
        User user = userRepository.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND , "User not found"));
        if (update.name() != null) user.setName(update.name().trim());

        if (update.email() != null) {
            String email = update.email().trim().toLowerCase();
            if (!email.equals(user.getEmail()) && existsByEmail(email)) throw new ApiException(HttpStatus.CONFLICT, "Email already exists");
            user.setEmail(email);
        }

        if (update.active() != null) user.setActive(update.active());
        if (update.role() != null) user.setRole(update.role());
        

        User savedUser = userRepository.save(user);

        return UserMapper.toResponse(savedUser);
    }

    public void deleteUser(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND ,"User not found"));
        userRepository.delete(user);
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND ,"User not found"));
        return UserMapper.toResponse(user);
    }

    public List<UserResponse> getAllUsers(){
        List<User> users = userRepository.findAll();
        return users.stream().map(UserMapper::toResponse).toList();
    }

    public UserResponse getUserByEmail(String email){
        User user = userRepository.findByEmail(email.trim().toLowerCase()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND ,"User not found"));
        return UserMapper.toResponse(user);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email.trim().toLowerCase());
    }
    
}
