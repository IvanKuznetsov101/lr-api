package com.vsu.events_spring.controller;

import com.vsu.events_spring.dto.response.EventDTO;
import com.vsu.events_spring.dto.response.ImageDTO;
import com.vsu.events_spring.repository.ImageRepository;
import com.vsu.events_spring.service.ImageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/api/images")
@AllArgsConstructor
public class ImageController {
    private final ImageService imageService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ImageRepository imageRepository;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImages(@RequestParam("images") List<MultipartFile> files,
                                               @RequestParam(value = "profileId", required = false) Long profileId,
                                               @RequestParam(value = "eventId", required = false) Long eventId) {
        imageService.saveImages(files, profileId, eventId);
        return ResponseEntity.ok("Images uploaded successfully");
    }
    @PutMapping("/update")
    public ResponseEntity<String> updateImages(
            @RequestPart(value = "images") List<MultipartFile> files,
            @RequestParam(value = "profileId", required = false) Long profileId,
            @RequestParam(value = "eventId", required = false) Long eventId) {
        imageService.updateImages(files, profileId, eventId);
        return ResponseEntity.ok("Images updated successfully");
    }
    @DeleteMapping("/{ids}")
    public ResponseEntity<List<Long>> deleteImagesByIds(@PathVariable("ids") String ids) {
        if (ids == null || ids.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        imageService.deleteByIds(idList);
        return ResponseEntity.ok().body(idList);
    }
    @GetMapping(value = "/{imageId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> downloadImage(@PathVariable Long imageId) {
        byte[] imageData = imageService.getImageById(imageId);
        if (imageData == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageData);
    }

    @GetMapping(value = "/profile/{profileId}")
    public ResponseEntity<String> getImageByProfileId(@PathVariable Long profileId) {
        String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "") + "/api/images";
        String link = imageService.getImageByProfileId(profileId, baseUrl);
        if (link == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok().body("\"" + link + "\"");
    }

    @GetMapping(value = "/event/{eventId}")
    public ResponseEntity<List<Long>> getImagesByEventId(@PathVariable Long eventId) {
        List<Long> images = imageService.getIdsImagesByEventId(eventId);
        if (images.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok().body(images);
    }


    @GetMapping(value = "/event/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE,  params = "files")
    public ResponseEntity<List<ImageDTO>> downloadImagesByEventId(@PathVariable Long eventId) {
        List<ImageDTO> images = imageService.getImagesByEventId(eventId);
        if (images.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
        return ResponseEntity.ok(images);
    }
    @GetMapping(value = "/event/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE,  params = "links")
    public ResponseEntity<List<String>> getLinksImagesByEventId(@PathVariable Long eventId) {
        String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "") + "/api/images";

        List<String> images = imageService.getLinksImagesByEventId(eventId, baseUrl);
        if (images.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
        return ResponseEntity.ok(images);
    }

}
