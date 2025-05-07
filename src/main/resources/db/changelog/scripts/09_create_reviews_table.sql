CREATE TABLE reviews (
    id_review BIGSERIAL PRIMARY KEY,
    from_profile_id BIGINT NOT NULL,
    to_profile_id BIGINT NOT NULL,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    text TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_from_user FOREIGN KEY (from_profile_id) REFERENCES profile(id_profile) ON DELETE CASCADE,
    CONSTRAINT fk_to_user FOREIGN KEY (to_profile_id) REFERENCES profile(id_profile) ON DELETE CASCADE,
    CONSTRAINT chk_users CHECK (from_profile_id <> to_profile_id)
);