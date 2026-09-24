ALTER TABLE password_reset_tokens
DROP CONSTRAINT fk_password_reset_user;

ALTER TABLE password_reset_tokens
    ADD CONSTRAINT fk_password_reset_user
        FOREIGN KEY (user_id)
            REFERENCES users(id)
            ON DELETE CASCADE;