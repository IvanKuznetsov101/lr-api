package com.vsu.events_spring.repository;

import com.vsu.events_spring.entity.Visitor;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class VisitorRepository {

    private final JdbcTemplate jdbcTemplate;

    public Long create(Visitor visitor) {
        String sql = "INSERT INTO visitor (id_light_room, id_profile, visitor_start_time, visitor_end_time) VALUES (?, ?, ?, ?) RETURNING id_visitor";
        Object[] params = new Object[]{visitor.getId_light_room(), visitor.getId_profile(), visitor.getVisitor_start_time(), visitor.getVisitor_end_time()};
        return jdbcTemplate.queryForObject(sql, params, Long.class);
    }

    public void delete(Long id) {
        String sql = "DELETE FROM visitor WHERE id_visitor = ?";
        jdbcTemplate.update(sql, id);
    }

    public Visitor findById(Long id) {
        try {
            String sql = "SELECT * FROM visitor WHERE id_visitor = ?";
            return jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(Visitor.class), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Visitor> findAll() {
        String sql = "SELECT * FROM visitor ";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Visitor.class));
    }

    public void updateEndTimeById(Long idVisitor) {
        String sql = "UPDATE visitor SET visitor_end_time = current_timestamp AT TIME ZONE 'UTC' WHERE id_visitor = ?";
        jdbcTemplate.update(sql, idVisitor);
    }
    public Visitor getCurrentVisitorByProfileId(Long idProfile){
        try{
            String sql = "SELECT * FROM visitor WHERE id_profile = ? AND visitor_end_time >= current_timestamp AT TIME ZONE 'UTC'";
            return jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(Visitor.class), idProfile);
        } catch (EmptyResultDataAccessException e) {
        return null;
    }
    }
    public Long getVisitorCountByLightRoomId(Long lightRoomId){
        String sql = "SELECT COUNT(id_visitor) FROM visitor\n" +
                "WHERE id_light_room = ? AND visitor_end_time >= current_timestamp AT TIME ZONE 'UTC'";
        Long count = jdbcTemplate.queryForObject(sql, Long.class, lightRoomId);
        return count != null ? count : 0L;
    }
    public void setEndTimeByLightRoomId(Long id){
        String sql = """
                UPDATE visitor
                SET visitor_end_time = current_timestamp AT TIME ZONE 'UTC'
                WHERE id_light_room = ? AND visitor_end_time >= current_timestamp AT TIME ZONE 'UTC'
                """;
        jdbcTemplate.update(sql, id);
    }
}
