package com.vsu.events_spring.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class LightRoomDTO {
    private Long id;
    private double latitude;
    private double longitude;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
