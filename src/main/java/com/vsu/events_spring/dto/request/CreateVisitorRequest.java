package com.vsu.events_spring.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateVisitorRequest {
    @NotNull
    private Long profileId;
    @NotNull
    private Long lightRoomId;
}