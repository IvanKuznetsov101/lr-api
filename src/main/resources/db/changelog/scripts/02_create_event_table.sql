CREATE TABLE event (
    id_event BIGSERIAL PRIMARY KEY,
    title TEXT,
    description TEXT,
    age_limit INTEGER,
    profile_id BIGINT REFERENCES profile(id_profile)
);