CREATE TABLE visitor (
    id_visitor BIGSERIAL PRIMARY KEY,
    visitor_start_time timestamp with time zone,
    visitor_end_time timestamp with time zone,
    id_light_room BIGINT REFERENCES light_room(id_light_room),
    id_profile BIGINT REFERENCES profile(id_profile)
);