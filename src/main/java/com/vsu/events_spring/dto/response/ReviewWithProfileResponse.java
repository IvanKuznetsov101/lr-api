package com.vsu.events_spring.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class ReviewWithProfileResponse {
    private Long id;
    private Long fromProfileId;
    private Long toProfileId;
    private int rating;
    private String text;
    private LocalDate createdAt;
    private String fullName;
    private String image;
}
