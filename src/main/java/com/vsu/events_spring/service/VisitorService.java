package com.vsu.events_spring.service;

import com.vsu.events_spring.dto.response.VisitorDTO;
import com.vsu.events_spring.dto.request.CreateVisitorRequest;
import com.vsu.events_spring.entity.LightRoom;
import com.vsu.events_spring.entity.Profile;
import com.vsu.events_spring.entity.Visitor;
import com.vsu.events_spring.exception.LightRoomNotFountException;
import com.vsu.events_spring.exception.ProfileNotFountException;
import com.vsu.events_spring.repository.LightRoomRepository;
import com.vsu.events_spring.repository.ProfileRepository;
import com.vsu.events_spring.repository.VisitorRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@AllArgsConstructor
public class VisitorService {
    private VisitorRepository visitorRepository;
    private ProfileRepository profileRepository;
    private VisitorMapperService visitorMapperService;
    private LightRoomRepository lightRoomRepository;

    public VisitorDTO createVisitor(CreateVisitorRequest createVisitorRequest) {
        LightRoom lightRoom = lightRoomRepository.findById(createVisitorRequest.getLightRoomId());
        VisitorDTO visitor = getCurrentVisitorByProfileId(createVisitorRequest.getProfileId());
        if (visitor != null){
            visitorRepository.updateEndTimeById(visitor.getIdVisitor());
        }
        Visitor visitorCreate = Visitor.builder()
                        .visitor_start_time(LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC))
                        .visitor_end_time(lightRoom.getLight_room_end_time())
                        .id_profile(createVisitorRequest.getProfileId())
                        .id_light_room(createVisitorRequest.getLightRoomId())
                        .build();
        Long idVisitor = visitorRepository.create(visitorCreate);
        return VisitorDTO.builder()
                .idProfile(createVisitorRequest.getProfileId())
                .idLightRoom(createVisitorRequest.getLightRoomId())
                .idVisitor(idVisitor)
                .build();
    }
    public VisitorDTO updateEndTimeVisitor(Long idVisitor) {
        visitorRepository.updateEndTimeById(idVisitor);
        return VisitorDTO.builder()
                .idVisitor(idVisitor)
                .build();
    }
    public VisitorDTO getCurrentVisitorByProfileId(Long profileId){
        if (profileRepository.findById(profileId) == null){
            throw new ProfileNotFountException("idProfile:" + profileId);
        }
        Visitor visitor = visitorRepository.getCurrentVisitorByProfileId(profileId);
        if (visitor == null){
            return null;
        }
        return visitorMapperService.toDTO(visitor);
    }
    public Long getVisitorCountByLightRoomId(Long lightRoomId){
        if( lightRoomRepository.findById(lightRoomId) == null){
            throw new LightRoomNotFountException("idLightRoom:" + lightRoomId);
        }
        return visitorRepository.getVisitorCountByLightRoomId(lightRoomId);
    }
}