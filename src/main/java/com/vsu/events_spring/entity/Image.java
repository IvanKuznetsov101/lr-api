package com.vsu.events_spring.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    private Long idImage;
    private Long idProfile;
    private Long idEvent;
    private byte[] image;
}
