package com.vsu.events_spring.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class ExtendedProfileDTO {
    private Long id;
    private String fullName;
    private String username;
    private String email;
    private LocalDate date_of_birth;
}
