package com.vsu.events_spring.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class EventDTO {
    private Long id;
    private String title;
    private String description;
    private int ageLimit;
}
