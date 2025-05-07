package com.vsu.events_spring.service;

import com.vsu.events_spring.configuration.AppConfig;
import com.vsu.events_spring.dto.response.EventDTO;
import com.vsu.events_spring.dto.response.LightRoomDTO;
import com.vsu.events_spring.entity.Event;
import com.vsu.events_spring.entity.LightRoom;
import com.vsu.events_spring.exception.ProfileNotFountException;
import com.vsu.events_spring.repository.LightRoomRepository;
import com.vsu.events_spring.dto.request.CreateLightRoomRequest;
import com.vsu.events_spring.dto.request.GetPointsInAreaRequest;
import com.vsu.events_spring.repository.ProfileRepository;
import com.vsu.events_spring.repository.VisitorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@AllArgsConstructor
public class LightRoomService {
    private final LightRoomMapperService lightRoomMapperService;
    private final AppConfig appConfig;
    private final EventService eventService;
    private final VisitorRepository visitorRepository;

    private LightRoomRepository lightRoomRepository;
    private ProfileRepository profileRepository;

    public LightRoomDTO createNewLightRoom(CreateLightRoomRequest createLightRoomRequest) {
        String pointWKT = "POINT(" + createLightRoomRequest.getLongitude() + " " + createLightRoomRequest.getLatitude() + ")";
        Instant now = Instant.now();
        LocalDateTime startTime = LocalDateTime.ofInstant(now, ZoneOffset.UTC);
        LocalDateTime endTime = startTime.plusHours(appConfig.getLifetime());
        LightRoom newLightRoom = LightRoom
                .builder()
                .light_room_coordinates(pointWKT)
                .light_room_start_time(startTime)
                .light_room_end_time(endTime)
                .id_event(createLightRoomRequest.getEventId())
                .build();
        Long id = lightRoomRepository.create(newLightRoom);
        return lightRoomMapperService.toDTO(createLightRoomRequest, id);
    }

    public LightRoomDTO deleteLightRoom(Long id) {
        LightRoom lightRoom = lightRoomRepository.findById(id);
        if (lightRoom == null) {
            throw new ProfileNotFountException("idLightRoom:" + id);
        }
        Instant now = Instant.now();
        LocalDateTime endTime = LocalDateTime.ofInstant(now, ZoneOffset.UTC);
        lightRoomRepository.update(id, endTime);
        visitorRepository.setEndTimeByLightRoomId(id);
        return lightRoomMapperService.toDTO(lightRoom);
    }

    public LightRoomDTO getLightRoom(Long id) {
        LightRoom lightRoom = lightRoomRepository.findById(id);
        if (lightRoom == null) {
            throw new ProfileNotFountException("idLightRoom:" + id);
        }
        return lightRoomMapperService.toDTO(lightRoom);
    }

    public List<LightRoomDTO> getAllLightRooms() {
        List<LightRoom> lightRooms = lightRoomRepository.findAll();
        return lightRoomMapperService.toLightRoomsDTO(lightRooms);
    }

    public List<LightRoomDTO> getLightRoomsInArea(GetPointsInAreaRequest getPointsInAreaRequest) {
        List<LightRoom> lightRooms = lightRoomRepository.findByArea(getPointsInAreaRequest);
        return lightRoomMapperService.toLightRoomsDTO(lightRooms);
    }
    public LightRoomDTO getLightRoomByEventId(Long eventId){
        return lightRoomMapperService.toDTO(lightRoomRepository.findByEventId(eventId));
    }
}