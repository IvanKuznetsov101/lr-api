package com.vsu.events_spring.service;

import com.vsu.events_spring.dto.response.ImageDTO;
import com.vsu.events_spring.entity.Image;
import com.vsu.events_spring.repository.EventRepository;
import com.vsu.events_spring.repository.ImageRepository;
import com.vsu.events_spring.repository.ProfileRepository;
import com.vsu.events_spring.dto.request.SaveImageRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final EventRepository eventRepository;
    private final ProfileRepository profileRepository;

    public void saveImage(SaveImageRequest saveImageRequest) {
        try {
            Image image = Image.builder()
                    .idEvent(saveImageRequest.getEventId())
                    .idProfile(saveImageRequest.getProfileId())
                    .image(saveImageRequest.getFile().getBytes())
                    .build();
            imageRepository.saveImage(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveImages(List<MultipartFile> files, Long profileId, Long eventId) {
        List<Image> images = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                Image image = Image.builder()
                        .idEvent(eventId)
                        .idProfile(profileId)
                        .image(file.getBytes())
                        .build();
                images.add(image);
            } catch (IOException e) {
                throw new RuntimeException("Error processing file", e);
            }
        }
        imageRepository.saveImages(images);
    }
    public void updateImages(List<MultipartFile> files, Long profileId, Long eventId) {
        if(profileId != null && profileRepository.findById(profileId) != null)
            imageRepository.deleteByProfileId(profileId);

        if (eventId != null && eventRepository.findById(eventId) != null)
            imageRepository.deleteByEventId(eventId);

        List<Image> images = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                Image image = Image.builder()
                        .idEvent(eventId)
                        .idProfile(profileId)
                        .image(file.getBytes())
                        .build();
                images.add(image);
            } catch (IOException e) {
                throw new RuntimeException("Error processing file", e);
            }
        }
        imageRepository.saveImages(images);
    }
    public String getImageByProfileId(Long profileId, String baseUrl) {
        Long id = imageRepository.findImageByProfileId(profileId);
        if (id != null){
            return baseUrl + "/" + id;
        }
        return null;
    }

    public byte[] getImageById(Long Id) {
        return imageRepository.findImageById(Id);
    }
    public void deleteByIds(List<Long> id){
        imageRepository.deleteByIds(id);
    }

    public List<Long> getIdsImagesByEventId(Long eventId) {
        return imageRepository.findImagesIdsByEventId(eventId);
    }
    public List<ImageDTO> getImagesByEventId(Long eventId) {
        return imageRepository.findImagesByEventId(eventId);
    }
    public List<String> getLinksImagesByEventId(Long eventId, String baseUrl) {
        List<Long> ids = imageRepository.findImagesIdsByEventId(eventId);
        return ids.stream()
                .map(id -> baseUrl + "/" + id)
                .collect(Collectors.toList());
    }
}
