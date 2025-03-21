package com.vsu.events_spring.dto.request;

import lombok.Data;

@Data
public class JwtRequest {
    private String username;
    private String password;
}