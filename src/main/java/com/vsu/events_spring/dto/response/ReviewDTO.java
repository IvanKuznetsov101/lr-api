package com.vsu.events_spring.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private Long id;
    private Long from_user_id;
    private Long to_user_id;
    private String text;
    private int rating;
    private LocalDate created_at;
}
