package com.vsu.events_spring.repository;

import com.vsu.events_spring.dto.response.ImageDTO;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class RefreshTokenRepository {

    private final JdbcTemplate jdbcTemplate;

    public void saveToken(String token, Long profileId) {
        String sql = "INSERT INTO refresh_tokens (token, user_id) VALUES (?, ?)";
        Object[] params = new Object[]{token, profileId};
        jdbcTemplate.update(sql, params);
    }
    public String findByProfileId(Long profileId) {
        String sql = "SELECT token FROM refresh_tokens WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, profileId);
    }
    public void updateRefreshToken(Long profileId, String newToken) {
        String sql = "UPDATE refresh_tokens SET token = ? WHERE user_id = ?";
        jdbcTemplate.update(sql, newToken, profileId);
    }
    public Integer checkRefreshToken(Long profileId) {
        String sql = "SELECT COUNT(*) FROM refresh_tokens WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, profileId);
    }
}
