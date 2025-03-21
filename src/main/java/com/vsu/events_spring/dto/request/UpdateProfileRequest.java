package com.vsu.events_spring.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateProfileRequest {
    @Size(min = 5, max = 30)
    @NotBlank
    private String fullName;
    @NotBlank
    @Size(min = 5, max = 30)
    private String username;
    @NotBlank
    @Size(min = 5, max = 30)
    private String password;
    @NotBlank
    @Email
    private String email;
    @NotNull
    private LocalDate date_of_birth;
}