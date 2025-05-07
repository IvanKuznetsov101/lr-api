package com.vsu.events_spring.repository;

import com.vsu.events_spring.configuration.AppConfig;
import com.vsu.events_spring.entity.Event;
import com.vsu.events_spring.entity.LastAttendedEvent;
import com.vsu.events_spring.entity.LightRoom;
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
public class EventRepository {

    private final JdbcTemplate jdbcTemplate;
    private final AppConfig appConfig;

    public Long create(Event event) {
        String sql = "INSERT INTO event (title, description, age_limit, profile_id) VALUES (?, ?, ?, ?) RETURNING id_event";
        Object[] params = new Object[]{event.getTitle(), event.getDescription(), event.getAge_limit(), event.getProfile_id()};
        return jdbcTemplate.queryForObject(sql, params, Long.class);
    }

    public void update(Event event) {
        String sql = "UPDATE event SET title = ?, description = ?, age_limit = ?, profile_id = ? WHERE id_event= ?";
        Object[] params = new Object[]{event.getTitle(), event.getDescription(), event.getAge_limit(), event.getProfile_id(), event.getId_event()};
        jdbcTemplate.update(sql, params);
    }

    public void delete(Long id) {
        String sql = "UPDATE event SET is_deleted = true WHERE id_event= ?";
        jdbcTemplate.update(sql, id);
    }

    public Event findById(Long id) {
        try {
            String sql = "SELECT * FROM event WHERE id_event = ?";
            return jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(Event.class), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Event> findAll() {
        String sql = "SELECT * FROM event";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Event.class));
    }

    public List<Event> findByProfileId(Long profileId) {
        String sql = "SELECT * FROM event WHERE profile_id = ?";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Event.class), profileId);
    }

    public Event findByLightRoomId(Long lightRoomId){
        String sql = "SELECT e.id_event, e.profile_id, e.title, e.description, e.age_limit " +
                "FROM light_room lr " +
                "JOIN event e ON lr.id_event = e.id_event " +
                "WHERE lr.id_light_room = ?;";
        return jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(Event.class), lightRoomId);
    }
    public List<Event> findNotUsedByProfileId(Long id) {
        try {
            String sql = "SELECT e.* FROM event e WHERE e.profile_id = ? " +
                    "AND e.is_deleted = false " +
                    "AND e.id_event NOT IN (" +
                    "SELECT lr.id_event " +
                    "FROM light_room lr " +
                    "WHERE lr.light_room_end_time >= CURRENT_TIMESTAMP)"
                    ;
            return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Event.class), id);
        } catch (DataAccessException e) {
            return Collections.emptyList();
        }
    }
    public Event findActualEventByProfileId(Long id) {
        String sql = "SELECT e.* FROM event e WHERE e.profile_id = ? " +
                    "AND e.id_event IN (" +
                    "SELECT lr.id_event " +
                    "FROM light_room lr " +
                    "WHERE lr.light_room_end_time >= CURRENT_TIMESTAMP)";
        return  jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(Event.class), id);
    }
    public List<Event> findByProfileCoordinates(String profileCoordinates) {
        try {
            int radius = appConfig.getRadius();
            String sql = "SELECT * FROM event WHERE id_event in ("+
                    "SELECT id_event FROM light_room lr " +
                    "WHERE ST_DWithin(ST_GeomFromText(?, 4326), lr.light_room_coordinates, ?) " +
                    "AND light_room_end_time >= current_timestamp AT TIME ZONE 'UTC')";
            Object[] params = new Object[]{profileCoordinates, radius};
            return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Event.class), params);
        } catch (DataAccessException e) {
            return Collections.emptyList();
        }
    }
    public List<LastAttendedEvent> findTheLastAttendedEvents(Long profileId) {
        String sql = """
               
                WITH latest_events AS (
                                             SELECT DISTINCT ON (e.id_event)
                                                 e.id_event,
                                                 e.title,
                                                 e.profile_id,
                                                 i.id_image,
                                                 v.visitor_end_time
                                             FROM event e
                                             JOIN light_room l ON l.id_event = e.id_event
                                             JOIN visitor v ON v.id_light_room = l.id_light_room
                                             LEFT JOIN image i ON e.id_event = i.id_event
                                             WHERE v.id_profile = ? AND v.visitor_end_time IS NOT NULL AND 
                                                e.profile_id != v.id_profile
                                             ORDER BY e.id_event, v.visitor_end_time DESC, i.id_image
                                         )
                                         SELECT * FROM latest_events
                                         ORDER BY visitor_end_time DESC
                                         LIMIT 10;
                """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(LastAttendedEvent.class), profileId);
    }
}
