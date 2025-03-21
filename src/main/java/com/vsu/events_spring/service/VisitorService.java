package com.vsu.events_spring.service;

import com.vsu.events_spring.dto.response.VisitorDTO;
import com.vsu.events_spring.dto.request.CreateVisitorRequest;
import com.vsu.events_spring.entity.Visitor;
import com.vsu.events_spring.repository.VisitorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@AllArgsConstructor
public class VisitorService {
    private VisitorRepository visitorRepository;

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
                .idVisitor(idVisitor)
                .idLightRoom(createVisitorRequest.getLightRoomId())
                .build();
    }
    public VisitorDTO updateEndTimeVisitor(Long idVisitor) {
        visitorRepository.updateEndTimeById(idVisitor);
        return VisitorDTO.builder()
                .idVisitor(idVisitor)
                .build();
    }
}