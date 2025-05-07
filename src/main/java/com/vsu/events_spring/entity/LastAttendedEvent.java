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
public class LastAttendedEvent {
    private Long id_event;
    private String title;
    private Long profile_id;
    private Long id_image;
    private LocalDateTime visitor_end_time;
}