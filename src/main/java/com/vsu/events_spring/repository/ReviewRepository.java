package com.vsu.events_spring.repository;

import com.vsu.events_spring.dto.response.ReviewDTO;
import com.vsu.events_spring.entity.*;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

@Repository
@AllArgsConstructor
public class ReviewRepository {

    private final JdbcTemplate jdbcTemplate;

    public Long create(Review review) {
        String sql = "INSERT INTO reviews (from_profile_id, to_profile_id, rating, text, created_at) VALUES (?, ?, ?, ?, ?) RETURNING id_review";
        Object[] params = new Object[]{review.getFrom_user_id(), review.getTo_user_id(), review.getRating(), review.getText(), review.getCreated_at()};
        return jdbcTemplate.queryForObject(sql, params, Long.class);
    }

    public Long update(Review review) {
        String sql = "UPDATE reviews SET rating = ?, text = ?, created_at = ? WHERE from_profile_id = ? AND to_profile_id = ? RETURNING id_review";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, review.getRating());
            ps.setString(2, review.getText());
            ps.setTimestamp(3, Timestamp.valueOf(review.getCreated_at().atStartOfDay()));
            ps.setLong(4, review.getFrom_user_id());
            ps.setLong(5, review.getTo_user_id());
            return ps;
        }, keyHolder);
        return keyHolder.getKey() != null ? keyHolder.getKey().longValue() : null;
    }

    public List<Review> findByProfileId(Long profileId) {
        try {
            String sql = "SELECT * FROM reviews WHERE to_profile_id = ?";
            return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Review.class), profileId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    public Review findByIds(Long fromProfileId, Long toProfileId) {
        String sql = """     
        SELECT * FROM reviews WHERE to_profile_id = ? AND from_profile_id = ?
        """;
        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{toProfileId, fromProfileId},
                    BeanPropertyRowMapper.newInstance(Review.class)
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    public List<Integer> findRatingsByProfileId(Long profileId) {
        String sql = "SELECT rating FROM reviews WHERE to_profile_id = ?";
        return jdbcTemplate.queryForList(sql, Integer.class, profileId);
    }

    public List<ReviewWithProfile> findReviewsWithProfilesByProfileId(Long profileId) {
        String sql = """
                SELECT r.id_review, r.from_profile_id,
                                r.to_profile_id, r.rating, r.text, r.created_at,
                                p.full_name, i.id_image
                                FROM reviews r
                                JOIN profile p ON r.from_profile_id = p.id_profile
                                LEFT JOIN image i ON r.from_profile_id = i.id_profile
                                WHERE to_profile_id = ?;""";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ReviewWithProfile.class), profileId);
    }



}
