package com.vsu.events_spring.service;

import com.vsu.events_spring.dto.response.ProfileDTO;
import com.vsu.events_spring.entity.Profile;
import org.springframework.stereotype.Service;

@Service
public class ProfileMapperService {
    public ProfileDTO toDTO(Profile profile, Long id){
        return ProfileDTO
                .builder()
                .id(id)
                .fullName(profile.getFull_name())
                .username(profile.getUsername())
                .build();
    }
    public ProfileDTO toDTO(Profile profile){
        return ProfileDTO
                .builder()
                .fullName(profile.getFull_name())
                .username(profile.getUsername())
                .build();
    }
}

