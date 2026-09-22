package com.project.project.auth.service;

import com.project.project.auth.dto.LoginResponse;
import com.project.project.auth.entity.PasswordResetToken;
import com.project.project.auth.repository.PasswordResetTokenRepository;
import com.project.project.common.exception.ApiException;
import com.project.project.email.dto.EmailMessage;
import com.project.project.email.queue.EmailProducer;
import com.project.project.user.entity.User;
import com.project.project.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    private EmailProducer emailProducer;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public LoginResponse login(String email, String password){
        email = email.trim().toLowerCase();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND ,"User not found"));
        String token = jwtService.generateToken(user);
        return new LoginResponse(token);
    }

    public String forgetPassword(String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND ,"User not found"));
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expireAt(LocalDateTime.now().plusMinutes(15))
                .used(false)
                .build();

        passwordResetTokenRepository.save(resetToken);
        EmailMessage emailMessage = new EmailMessage(
                user.getEmail(),
                "Reset Password",
                "email/auth/password-reset",
                Map.of(
                        "name", user.getName(),
                        "token", token
                )
        );
        emailProducer.sendEmail(emailMessage);

        return "Password email send successfully";
    }

    public String resetPassword(String password, String token){
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND ,"Token not found or expired"));

        if(resetToken.isUsed()) throw new ApiException(HttpStatus.CONFLICT, "Token has already been used");
        if(resetToken.getExpireAt().isBefore(LocalDateTime.now())) throw new ApiException(HttpStatus.CONFLICT, "Token has expired");

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);

        return "Password reset successfully";
    }

    public String changePassword(String oldPassword, String newPassword){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) throw new ApiException( HttpStatus.UNAUTHORIZED, "User is not authenticated");
        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND ,"User not found"));
        if(!passwordEncoder.matches(oldPassword,user.getPassword())) throw new ApiException(HttpStatus.UNAUTHORIZED, "Old password does not match this one");

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return "Password changed successfully";
    }
}
