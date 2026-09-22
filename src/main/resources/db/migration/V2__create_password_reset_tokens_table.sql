CREATE TABLE password_reset_tokens(
    id        BIGSERIAL PRIMARY KEY,
    token     VARCHAR(255) NOT NULL UNIQUE,
    user_id   BIGINT       NOT NULL,
    expire_at TIMESTAMP    NOT NULL,
    used      BOOLEAN      NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_password_reset_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
);