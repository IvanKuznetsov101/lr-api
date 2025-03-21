CREATE TABLE light_room (
    id_light_room BIGSERIAL PRIMARY KEY,
    light_room_coordinates GEOGRAPHY(POINT,4326),
    light_room_start_time timestamp with time zone,
    light_room_end_time timestamp with time zone,
    id_event BIGINT REFERENCES event(id_event)
);