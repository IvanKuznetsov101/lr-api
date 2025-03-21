package com.vsu.events_spring.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LightRoom {
    private Long id_light_room;
    private String light_room_coordinates;
    private LocalDateTime light_room_start_time;
    private LocalDateTime light_room_end_time;
    private Long id_event;
}
