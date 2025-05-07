package com.vsu.events_spring.controller;

import com.vsu.events_spring.dto.request.CreateReviewRequest;
import com.vsu.events_spring.dto.response.AverageRatingWithCount;
import com.vsu.events_spring.dto.response.ReviewWithProfileResponse;
import com.vsu.events_spring.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/reviews")
@AllArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    @Autowired
    private HttpServletRequest request;

    @PostMapping
    public ResponseEntity<Long> createReview(@Valid @RequestBody CreateReviewRequest createReviewRequest) {
        Long reviewId = reviewService.createNewReview(createReviewRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewId);
    }

    @GetMapping(value = "/profile/{profileId}")
    public ResponseEntity<List<ReviewWithProfileResponse>> getReviewsByProfileId(@PathVariable Long profileId) {
        String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "") + "/api/images";
        List<ReviewWithProfileResponse> reviews = reviewService.getReviewsWithProfilesByProfileId(profileId, baseUrl);
        if (reviews.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok().body(reviews);
    }

    @GetMapping(value = "/average/{profileId}")
    public ResponseEntity<AverageRatingWithCount> getAverageRatingByProfileId(@PathVariable Long profileId) {
        AverageRatingWithCount averageRatingWithCount  = reviewService.getAverageRatingWithCount(profileId);
        return ResponseEntity.ok().body(averageRatingWithCount);
    }
    @GetMapping("/profile/{fromProfileId}/{toProfileId}")
    public ResponseEntity<ReviewWithProfileResponse> getReviewByProfileIds(
            @PathVariable Long fromProfileId,
            @PathVariable Long toProfileId) {
        String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "") + "/api/images";
        ReviewWithProfileResponse response = reviewService.getReviewWithProfiles(fromProfileId, toProfileId, baseUrl);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
