package com.vsu.events_spring.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignInRequest {
    @Size(min = 5, max = 30)
    private String username;
    @NotBlank
    @Size(min = 5, max = 30)
    private String password;
}