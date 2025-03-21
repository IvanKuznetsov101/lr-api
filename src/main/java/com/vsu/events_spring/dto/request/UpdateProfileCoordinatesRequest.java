package com.vsu.events_spring.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateProfileCoordinatesRequest {
    @NotBlank
    Long id;
    @NotNull
    double latitude;
    @NotNull
    double longitude;
}