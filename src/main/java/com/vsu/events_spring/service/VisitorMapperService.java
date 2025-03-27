package com.vsu.events_spring.service;

import com.vsu.events_spring.dto.request.CreateEventRequest;
import com.vsu.events_spring.dto.response.EventDTO;
import com.vsu.events_spring.dto.response.VisitorDTO;
import com.vsu.events_spring.entity.Event;
import com.vsu.events_spring.entity.Visitor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VisitorMapperService {
    public VisitorDTO toDTO(Visitor visitor){
        return VisitorDTO.builder()
                .idVisitor(visitor.getId_visitor())
                .idLightRoom(visitor.getId_light_room())
                .idProfile(visitor.getId_profile())
                .build();
    }
}
