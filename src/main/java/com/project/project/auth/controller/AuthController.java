package com.project.project.auth.controller;

import com.project.project.auth.dto.ChangePasswordRequest;
import com.project.project.auth.dto.LoginRequest;
import com.project.project.auth.dto.LoginResponse;
import com.project.project.auth.dto.ResetPasswordRequest;
import com.project.project.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest.email(), loginRequest.password());
    }

    @PostMapping("forget-password")
    public String forgetPassword(String email){
        return authService.forgetPassword(email);
    }

    @PostMapping("reset-password")
    public String resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest){
        return authService.resetPassword(resetPasswordRequest.password(), resetPasswordRequest.token());
    }

    @PostMapping("change-password")
    public String changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        return authService.changePassword(changePasswordRequest.oldPassword(),  changePasswordRequest.newPassword());
    }
}
