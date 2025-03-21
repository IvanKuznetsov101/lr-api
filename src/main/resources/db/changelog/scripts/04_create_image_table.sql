CREATE TABLE image (
    id_image BIGSERIAL PRIMARY KEY,
    id_event BIGINT REFERENCES event(id_event),
    id_profile BIGINT REFERENCES profile(id_profile),
    image BYTEA
);