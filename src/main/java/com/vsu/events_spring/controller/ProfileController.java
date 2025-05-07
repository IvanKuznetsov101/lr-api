package com.vsu.events_spring.controller;

import com.vsu.events_spring.dto.response.EventDTO;
import com.vsu.events_spring.dto.response.ExtendedProfileDTO;
import com.vsu.events_spring.dto.response.ProfileDTO;
import com.vsu.events_spring.dto.request.SignUpRequest;
import com.vsu.events_spring.dto.request.UpdateProfileCoordinatesRequest;
import com.vsu.events_spring.dto.request.UpdateProfileRequest;
import com.vsu.events_spring.service.ProfileService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/profiles")
@AllArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping
    public ResponseEntity<ProfileDTO> createProfile(@Valid @RequestBody SignUpRequest signUpRequest) {
        ProfileDTO createdProfile = profileService.createNewProfile(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProfile);
    }

    @PutMapping(value = "/coordinates", params = "type=get-ids")
    public ResponseEntity<List<Long>> updateProfileCoordinatesAndGetIds(@RequestBody UpdateProfileCoordinatesRequest updateProfileCoordinatesRequest) {
        try {
            List<Long> idLightRooms = profileService.updateCoordinatesAndGetIds(updateProfileCoordinatesRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(idLightRooms);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @PutMapping(value = "/coordinates", params = "type=get-events")
    public ResponseEntity<List<EventDTO>> updateProfileCoordinatesAndGetEvents(@RequestBody UpdateProfileCoordinatesRequest updateProfileCoordinatesRequest) {
        try {
            List<EventDTO> eventDTOS = profileService.updateCoordinatesAndGetEvents(updateProfileCoordinatesRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(eventDTOS);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProfileDTO> deleteProfile(@PathVariable(value = "id") Long id) {
        ProfileDTO profileDTO = profileService.deleteProfile(id);
        return ResponseEntity.status(HttpStatus.OK).body(profileDTO);
    }

    @GetMapping(value = "/{id}",  params = "type=short")
    public ResponseEntity<ProfileDTO> getProfileById(@PathVariable(value = "id") Long id) {
        ProfileDTO profileDTO = profileService.getProfile(id);
        return ResponseEntity.status(HttpStatus.OK).body(profileDTO);
    }
    @GetMapping(value = "/{id}",  params = "type=full")
    public ResponseEntity<ExtendedProfileDTO> getFullProfileById(@PathVariable(value = "id") Long id) {
        ExtendedProfileDTO extendedProfileDTO = profileService.getFullProfile(id);
        return ResponseEntity.status(HttpStatus.OK).body(extendedProfileDTO);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ProfileDTO> updateProfile(@PathVariable(value = "id") Long id, @Valid @RequestBody UpdateProfileRequest updateProfileRequest) {
        ProfileDTO profileDTO = profileService.updateProfile(id, updateProfileRequest);
        return ResponseEntity.status(HttpStatus.OK).body(profileDTO);
    }
    @GetMapping("/event/{id}")
    public ResponseEntity<ProfileDTO> getProfileByLightRoomId(@PathVariable(value = "id") Long id) {
        ProfileDTO profileDTO = profileService.getProfileByEventId(id);
        return ResponseEntity.status(HttpStatus.OK).body(profileDTO);
    }
}
