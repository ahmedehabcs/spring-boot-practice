package com.project.project.auth.entity;

import com.project.project.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_tokens")
@NoArgsConstructor
@Getter
@Setter
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime expireAt;

    @Column(nullable = false)
    private boolean used = false;


    @Builder
    public PasswordResetToken(String token, User user, LocalDateTime expireAt, boolean used) {
        this.token = token;
        this.user = user;
        this.expireAt = expireAt;
        this.used = used;
    }
}
