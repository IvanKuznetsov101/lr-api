package com.vsu.events_spring.repository;

import com.vsu.events_spring.entity.Event;
import com.vsu.events_spring.entity.Profile;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class ProfileRepository {

    private final JdbcTemplate jdbcTemplate;

    public Long create(Profile profile) {
        String sql = "INSERT INTO profile (full_name, username, password, email, date_of_birth) VALUES (?, ?, ?, ?, ?) RETURNING id_profile";
        Object[] params = new Object[]{profile.getFull_name(), profile.getUsername(), profile.getPassword(), profile.getEmail(), profile.getDate_of_birth()};
        return jdbcTemplate.queryForObject(sql, params, Long.class);
    }

    public Profile findByUserName(String login) {
        try {
            String sql = "SELECT * FROM profile WHERE username = ?";
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Profile.class), login);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Profile findById(Long id) {
        try {
            String sql = "SELECT * FROM profile WHERE id_profile = ?";
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Profile.class), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Profile> findAll() {
        String sql = "SELECT * FROM profile";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Profile.class));
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM profile WHERE id_profile = ?";
        return jdbcTemplate.update(sql, id);
    }

    public void update(Profile profile) {
        String sql = "UPDATE profile SET full_name = ?, username = ?, email = ?, date_of_birth = ?, password = ? WHERE id_profile = ?";
        Object[] params = new Object[]{profile.getFull_name(), profile.getUsername(), profile.getEmail(), profile.getDate_of_birth(), profile.getPassword(), profile.getId_profile()};
        jdbcTemplate.update(sql, params);
    }
    public void updateCoordinates(Long id,  String newCoordinates) {
        jdbcTemplate.update(
                "UPDATE profile SET profile_coordinates = ST_GeomFromText(?, 4326) WHERE id_profile = ?",
                newCoordinates, id
        );
    }
}
