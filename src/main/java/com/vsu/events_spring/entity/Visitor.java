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
public class Visitor {
    private Long id_visitor;
    private LocalDateTime visitor_start_time;
    private LocalDateTime visitor_end_time;
    private Long id_profile;
    private Long id_light_room;
}
