CREATE TABLE IF NOT EXISTS refresh_tokens
(
    id            BIGSERIAL PRIMARY KEY,
    refresh_token VARCHAR(512) NOT NULL UNIQUE,
    expiry_date   TIMESTAMP    NOT NULL,
    user_id       BIGINT       NOT NULL UNIQUE
    REFERENCES users (id) ON DELETE CASCADE -- при удалении пользователя токен тоже удалится
    );