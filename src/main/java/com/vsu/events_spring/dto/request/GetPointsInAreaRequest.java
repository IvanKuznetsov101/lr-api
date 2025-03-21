package com.vsu.events_spring.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GetPointsInAreaRequest {
    @NotNull
    private double swLat;
    @NotNull
    private double swLon;
    @NotNull
    private double neLat;
    @NotNull
    private double neLon;
}