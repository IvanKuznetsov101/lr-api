package com.vsu.events_spring.repository;

import com.vsu.events_spring.configuration.AppConfig;
import com.vsu.events_spring.entity.LightRoom;
import com.vsu.events_spring.dto.request.GetPointsInAreaRequest;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
@AllArgsConstructor
public class LightRoomRepository {

    private final JdbcTemplate jdbcTemplate;
    private final AppConfig appConfig;

    public Long create(LightRoom lightRoom) {
        String sql = "INSERT INTO light_room (light_room_coordinates, light_room_start_time, light_room_end_time, id_event) VALUES ( ST_GeomFromText(?, 4326), ?, ?, ?) RETURNING id_light_room";
        Object[] params = new Object[]{lightRoom.getLight_room_coordinates(), lightRoom.getLight_room_start_time(), lightRoom.getLight_room_end_time(), lightRoom.getId_event()};
        return jdbcTemplate.queryForObject(sql, params, Long.class);
    }

    public void delete(Long id) {
        String sql = "DELETE FROM light_room WHERE id_light_room = ?";
        jdbcTemplate.update(sql, id);
    }

    public LightRoom findById(Long id) {
        try {
            String sql = "SELECT * FROM light_room WHERE id_light_room = ?";
            return jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(LightRoom.class), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<LightRoom> findAll() {
        String sql = "SELECT * FROM light_room WHERE light_room_end_time >= current_timestamp AT TIME ZONE 'UTC'";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(LightRoom.class));
    }

    public List<Long> findByProfileCoordinates(String profileCoordinates) {
        try {
            int radius = appConfig.getRadius();
            String sql = "SELECT id_light_room FROM light_room lr " +
                    "WHERE ST_DWithin(ST_GeomFromText(?, 4326), lr.light_room_coordinates, ?) " +
                    "AND light_room_end_time >= current_timestamp AT TIME ZONE 'UTC'";
            Object[] params = new Object[]{profileCoordinates, radius};
            return jdbcTemplate.query(sql, params, (rs, rowNum) -> rs.getLong("id_light_room"));
        } catch (DataAccessException e) {
            return Collections.emptyList();
        }
    }


    public List<LightRoom> findByArea(GetPointsInAreaRequest getPointsInAreaRequest) {
        try {
            String sql = "SELECT * FROM light_room\n" +
                    "WHERE ST_Within(light_room_coordinates::geometry,\n" +
                    "ST_MakeEnvelope(?, ?, ?,  ?, 4326)) \n" +
                    "AND light_room_end_time >= current_timestamp AT TIME ZONE 'UTC'";
            Object[] params = new Object[]{
                    getPointsInAreaRequest.getNeLon(),
                    getPointsInAreaRequest.getNeLat(),
                    getPointsInAreaRequest.getSwLon(),
                    getPointsInAreaRequest.getSwLat()};
            return jdbcTemplate.query(sql, params, BeanPropertyRowMapper.newInstance(LightRoom.class));
        } catch (DataAccessException e) {
            return Collections.emptyList();
        }
    }

}
