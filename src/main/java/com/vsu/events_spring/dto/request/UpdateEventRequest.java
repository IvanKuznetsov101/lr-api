package com.vsu.events_spring.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateEventRequest {
    @NotBlank
    private String title;
    private String description;
    @NotNull
    private Integer ageLimit;
    @NotNull
    private Long profileId;
}