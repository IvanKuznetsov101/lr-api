package com.vsu.events_spring.service;

import com.vsu.events_spring.dto.response.ProfileDTO;
import com.vsu.events_spring.entity.Profile;
import com.vsu.events_spring.exception.LoginExistsException;
import com.vsu.events_spring.exception.ProfileNotFountException;
import com.vsu.events_spring.repository.LightRoomRepository;
import com.vsu.events_spring.repository.ProfileRepository;
import com.vsu.events_spring.dto.request.SignUpRequest;
import com.vsu.events_spring.dto.request.UpdateProfileCoordinatesRequest;
import com.vsu.events_spring.dto.request.UpdateProfileRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProfileService {
    private ProfileRepository profileRepository;
    private LightRoomRepository lightRoomRepository;
    private ProfileMapperService profileMapperService;
    private PasswordEncoder passwordEncoder;


    public ProfileDTO createNewProfile(SignUpRequest signUpRequest) {
        if (profileRepository.findByUserName(signUpRequest.getUsername()) == null) {
            Profile newProfile = Profile
                    .builder()
                    .full_name(signUpRequest.getFullName())
                    .username(signUpRequest.getUsername())
                    .password(passwordEncoder.encode(signUpRequest.getPassword()))
                    .email(signUpRequest.getEmail())
                    .date_of_birth(signUpRequest.getDate_of_birth())
                    .build();
            Long id = profileRepository.create(newProfile);
            return profileMapperService.toDTO(newProfile, id);
        }
        throw new LoginExistsException(signUpRequest.getUsername());
    }
    public ProfileDTO deleteProfile(Long id) {
        Profile profile = profileRepository.findById(id);
        if (profile == null) {
            throw new ProfileNotFountException("idProfile:" + id);
        }
        profileRepository.deleteById(id);
        return profileMapperService.toDTO(profile);
    }

    public ProfileDTO getProfile(Long id) {
        Profile profile = profileRepository.findById(id);
        if (profile == null) {
            throw new ProfileNotFountException("idProfile:" + id);
        }
        return profileMapperService.toDTO(profile);
    }

    public ProfileDTO getProfileByLogin(String login) {
        Profile profile = profileRepository.findByUserName(login);
        if (profile == null) {
            throw new ProfileNotFountException("login:" + login);
        }
        return profileMapperService.toDTO(profile);
    }

    public ProfileDTO updateProfile(Long id, UpdateProfileRequest updateProfileRequest) {
        Profile profile = profileRepository.findById(id);
        if (profile == null) {
            throw new ProfileNotFountException("idProfile:" + id);
        }
        Profile updatedProfile = Profile.builder()
                .full_name(updateProfileRequest.getFullName())
                .username(updateProfileRequest.getUsername())
                .password(updateProfileRequest.getPassword())
                .email(updateProfileRequest.getEmail())
                .build();
        profileRepository.update(updatedProfile);
        return profileMapperService.toDTO(updatedProfile);
    }

    public List<Long> updateCoordinates(UpdateProfileCoordinatesRequest updateProfileCoordinatesRequest) {
        String pointWKT = "POINT(" + updateProfileCoordinatesRequest.getLongitude() + " " + updateProfileCoordinatesRequest.getLatitude() + ")";
        profileRepository.updateCoordinates(updateProfileCoordinatesRequest.getId(), pointWKT);
        return lightRoomRepository.findByProfileCoordinates(pointWKT);
    }
}