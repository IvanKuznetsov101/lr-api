package com.vsu.events_spring.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AverageRatingWithCount {
    Double averageRating;
    Long count;
}
