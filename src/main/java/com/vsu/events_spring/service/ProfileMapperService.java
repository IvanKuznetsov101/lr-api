package com.vsu.events_spring.service;

import com.vsu.events_spring.dto.response.ExtendedProfileDTO;
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
                .id(profile.getId_profile())
                .fullName(profile.getFull_name())
                .username(profile.getUsername())
                .build();
    }
    public ExtendedProfileDTO toUpdateDTO(Profile profile){
        return ExtendedProfileDTO.builder()
                .id(profile.getId_profile())
                .fullName(profile.getFull_name())
                .username(profile.getUsername())
                .date_of_birth(profile.getDate_of_birth())
                .email(profile.getEmail())
                .build();
    }
}

