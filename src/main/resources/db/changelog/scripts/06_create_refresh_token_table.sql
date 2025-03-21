CREATE TABLE refresh_tokens (
    id_token BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES profile(id_profile),
    token TEXT NOT NULL
);