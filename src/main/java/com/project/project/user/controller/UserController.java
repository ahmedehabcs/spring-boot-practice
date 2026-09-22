package com.project.project.user.controller;

import com.project.project.common.ApiResponse;
import com.project.project.common.ResponseStatus;
import com.project.project.user.dto.CreateUserRequest;
import com.project.project.user.dto.UpdateUserRequest;
import com.project.project.user.dto.UserResponse;
import com.project.project.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN', 'EMPLOYEE')")
    @GetMapping
    public List<UserResponse> getAllUsers(){
        return userService.getAllUsers();
    }

    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN', 'EMPLOYEE')")
    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN', 'EMPLOYEE')")
    @GetMapping("/by-email")
    public UserResponse getUserByEmail(@RequestParam String email){
        return userService.getUserByEmail(email);
    }

    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN', 'EMPLOYEE')")
    @PostMapping
    public UserResponse createUser(@Valid @RequestBody CreateUserRequest createUserRequest){
        return userService.createUser(createUserRequest);
    }

    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    @PatchMapping("/{id}")
    public UserResponse updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest updateUserRequest){
        return userService.updateUser(id, updateUserRequest);
    }

    @PreAuthorize("hasAuthority('SUPERADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return new ApiResponse<>(ResponseStatus.SUCCESS, "User deleted Successfully", null);
    }
}