package com.vsu.events_spring.service;

import com.vsu.events_spring.dto.request.CreateReviewRequest;
import com.vsu.events_spring.dto.request.SignUpRequest;
import com.vsu.events_spring.dto.request.UpdateProfileCoordinatesRequest;
import com.vsu.events_spring.dto.request.UpdateProfileRequest;
import com.vsu.events_spring.dto.response.*;
import com.vsu.events_spring.entity.*;
import com.vsu.events_spring.exception.LoginExistsException;
import com.vsu.events_spring.exception.ProfileNotFountException;
import com.vsu.events_spring.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReviewService {
    private ReviewRepository reviewRepository;
    private ProfileRepository profileRepository;
    private ImageRepository imageRepository;


    public Long createNewReview(CreateReviewRequest createReviewRequest) {
        if (profileRepository.findById(createReviewRequest.getFrom_user_id()) == null){
            throw new ProfileNotFountException("idProfile:" + createReviewRequest.getFrom_user_id());
        }
        if (profileRepository.findById(createReviewRequest.getTo_user_id()) == null){
            throw new ProfileNotFountException("idProfile:" + createReviewRequest.getTo_user_id());
        }
        Instant now = Instant.now();
        LocalDate created_at = LocalDate.ofInstant(now, ZoneOffset.UTC);
        Review review = Review.builder()
                .from_user_id(createReviewRequest.getFrom_user_id())
                .to_user_id(createReviewRequest.getTo_user_id())
                .text(createReviewRequest.getText())
                .rating(createReviewRequest.getRating())
                .created_at(created_at)
                .build();
        if (reviewRepository.findByIds(createReviewRequest.getFrom_user_id(), createReviewRequest.getTo_user_id()) == null){
            return reviewRepository.create(review);
        }
        return reviewRepository.update(review);

    }
    public List<Review> getReviewsByProfileId(Long profileId) {
        if (profileRepository.findById(profileId) == null){
            throw new ProfileNotFountException("idProfile:" + profileId);
        }
        return reviewRepository.findByProfileId(profileId);
    }
    public AverageRatingWithCount getAverageRatingWithCount(Long profileId) {
        if (profileRepository.findById(profileId) == null){
            throw new ProfileNotFountException("idProfile:" + profileId);
        }
        List<Integer> ratings = reviewRepository.findRatingsByProfileId(profileId);
        if (ratings.isEmpty()) {
            return new AverageRatingWithCount(0.0, 0L);
        }
        Double averageRating = ratings.stream().mapToInt(Integer::intValue).average().orElse(0.0);
        Long count = (long) ratings.size();
        return new AverageRatingWithCount(averageRating, count);
    }
    public List<ReviewWithProfileResponse> getReviewsWithProfilesByProfileId(Long profileId, String baseUrl) {
        if (profileRepository.findById(profileId) == null){
            throw new ProfileNotFountException("idProfile:" + profileId);
        }
        List<ReviewWithProfile> reviews = reviewRepository.findReviewsWithProfilesByProfileId(profileId);
        return  reviews.stream()
                .map(review -> new ReviewWithProfileResponse(
                        review.getId_review(),
                        review.getFrom_profile_id(),
                        review.getTo_profile_id(),
                        review.getRating(),
                        review.getText(),
                        review.getCreated_at().toLocalDate(),
                        review.getFull_name(),
                        baseUrl + "/" + review.getId_image()
                ))
                .collect(Collectors.toList());
    }
    public ReviewWithProfileResponse getReviewWithProfiles(Long fromProfileId, Long toProfileId, String baseUrl) {
        if (profileRepository.findById(fromProfileId) == null){
            throw new ProfileNotFountException("idProfile:" + fromProfileId);
        }
        if (profileRepository.findById(toProfileId) == null){
            throw new ProfileNotFountException("idProfile:" + toProfileId);
        }
        Review rewiew = reviewRepository.findByIds(fromProfileId, toProfileId);
        Profile profile = profileRepository.findById(toProfileId);
        Long imageProfileId = imageRepository.findImageByProfileId(toProfileId);
        if (rewiew == null){
            return ReviewWithProfileResponse.builder()
                    .id(null)
                    .fromProfileId(fromProfileId)
                    .toProfileId(toProfileId)
                    .rating(0)
                    .text("")
                    .createdAt(null)
                    .fullName(profile.getFull_name())
                    .image(baseUrl + "/" + imageProfileId)
                    .build();
        }
        return ReviewWithProfileResponse.builder()
                .id(rewiew.getId())
                .fromProfileId(fromProfileId)
                .toProfileId(toProfileId)
                .rating(rewiew.getRating())
                .text(rewiew.getText())
                .createdAt(rewiew.getCreated_at())
                .fullName(profile.getFull_name())
                .image(baseUrl + "/" + imageProfileId)
                .build();
    }
}