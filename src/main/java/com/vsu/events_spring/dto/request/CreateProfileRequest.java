package com.vsu.events_spring.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateProfileRequest {
    @Size(min = 5, max = 30)
    @NotBlank
    private String fullName;
    @NotBlank
    @Size(min = 5, max = 30)
    private String login;
    @NotBlank
    @Size(min = 5, max = 30)
    private String password;
}