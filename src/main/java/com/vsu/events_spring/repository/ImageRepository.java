package com.vsu.events_spring.repository;

import com.vsu.events_spring.dto.response.ImageDTO;
import com.vsu.events_spring.entity.Image;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class ImageRepository {

    private final JdbcTemplate jdbcTemplate;

    public void saveImage(Image image) {
        String sql = "INSERT INTO image (id_profile, id_event, image) VALUES (?, ?, ?)";
        Object[] params = new Object[]{image.getIdProfile(), image.getIdEvent(), image.getImage()};
        jdbcTemplate.update(sql, params);
    }
    public void saveImages(List<Image> images) {
        String sql = "INSERT INTO image (id_profile, id_event, image) VALUES (?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, images, images.size(), (ps, image) -> {
            ps.setObject(1, image.getIdProfile());
            ps.setObject(2, image.getIdEvent());
            ps.setBytes(3, image.getImage());
        });
    }
    public Long findImageByProfileId(Long profileId) {
        String sql = "SELECT id_image FROM image WHERE id_profile = ? LIMIT 1";
        try {
            return jdbcTemplate.queryForObject(sql, Long.class, profileId);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }

    public byte[] findImageById(Long id) {
        String sql = "SELECT image FROM image WHERE id_image = ?";
        return jdbcTemplate.queryForObject(sql, new Long[]{id}, byte[].class);
    }

    public List<Long> findImagesIdsByEventId(Long eventId) {
        String sql = "SELECT id_image FROM image WHERE id_event = ?";
        return jdbcTemplate.queryForList(sql, new Long[]{eventId}, Long.class);
    }
    public List<ImageDTO> findImagesByEventId(Long eventId) {
        String sql = "SELECT id_image, image FROM image WHERE id_event = ?";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(ImageDTO.class), eventId);
    }
    public void deleteByProfileId(Long id) {
        String sql = "DELETE FROM image WHERE id_profile = ?";
        jdbcTemplate.update(sql, id);
    }

    public void deleteByEventId(Long id) {
        String sql = "DELETE FROM image WHERE id_event = ?";
        jdbcTemplate.update(sql, id);
    }
    public void deleteByIds(List<Long> id){
        if (id == null || id.isEmpty()) {
            return; // Nothing to delete
        }

        // Build the placeholders: one "?" for each ID
        String placeholders = String.join(",", id.stream().map(i -> "?").toList());
        String sql = "DELETE FROM image WHERE id_image IN (" + placeholders + ")";

        // Convert List<Long> to an array of Objects for JdbcTemplate
        Object[] params = id.toArray();

        // Execute the query
        jdbcTemplate.update(sql, params);
    }

}
