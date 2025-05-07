package com.vsu.events_spring.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    private Long id;
    private Long from_user_id;
    private Long to_user_id;
    private String text;
    private int rating;
    private LocalDate created_at;
}
