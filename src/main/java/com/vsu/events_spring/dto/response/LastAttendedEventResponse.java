package com.vsu.events_spring.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class LastAttendedEventResponse {
    private Long eventId;
    private String title;
    private Long profileId;
    private String image;
    private LocalDateTime visitorEndTime;
}