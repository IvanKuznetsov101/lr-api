package com.vsu.events_spring.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateEventRequest {
    @NotNull
    private String title;
    private String description;
    private Integer ageLimit;
    @NotNull
    private Long profileId;
}