package com.vsu.events_spring.service;

import com.vsu.events_spring.dto.response.VisitorDTO;
import com.vsu.events_spring.dto.request.CreateVisitorRequest;
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
        if(createVisitorRequest.getVisitorId()!= null){
            visitorRepository.updateEndTimeById(createVisitorRequest.getVisitorId());
        }
        Visitor visitor = Visitor.builder()
                        .visitor_start_time(LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC))
                        .id_profile(createVisitorRequest.getProfileId())
                        .id_light_room(createVisitorRequest.getLightRoomId())
                        .build();
        Long idVisitor = visitorRepository.create(visitor);
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
        return visitorMapperService.toDTO(visitorRepository.getCurrentVisitorByProfileId(profileId));
    }
    public Long getVisitorCountByLightRoomId(Long lightRoomId){
        if( lightRoomRepository.findById(lightRoomId) == null){
            throw new LightRoomNotFountException("idLightRoom:" + lightRoomId);
        }
        return visitorRepository.getVisitorCountByLightRoomId(lightRoomId);
    }
}