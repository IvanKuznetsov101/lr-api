package com.vsu.events_spring.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class SaveImageRequest {
    private Long profileId;
    private Long eventId;
    @NotNull
    MultipartFile file;
}