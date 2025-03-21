package com.vsu.events_spring.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateLightRoomRequest {
    @NotNull
    private double latitude;
    @NotNull
    private double longitude;
    @NotNull
    private Long eventId;
}