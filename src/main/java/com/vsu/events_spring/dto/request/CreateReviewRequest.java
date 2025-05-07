package com.vsu.events_spring.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateReviewRequest {
    @NotNull
    private Long from_user_id;
    @NotNull
    private Long to_user_id;
    @NotEmpty
    private String text;
    @NotNull
    private int rating;
}