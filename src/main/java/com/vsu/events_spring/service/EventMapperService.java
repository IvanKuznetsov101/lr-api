package com.vsu.events_spring.service;

import com.vsu.events_spring.dto.response.EventDTO;
import com.vsu.events_spring.entity.Event;
import com.vsu.events_spring.dto.request.CreateEventRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventMapperService {
    public EventDTO toDTO(CreateEventRequest createEventRequest, Long id){
        return EventDTO
                .builder()
                .id(id)
                .title(createEventRequest.getTitle())
                .description(createEventRequest.getDescription())
                .ageLimit(createEventRequest.getAgeLimit())
                .build();
    }
    public EventDTO toDTO(Event event){
        return EventDTO
                .builder()
                .id(event.getId_event())
                .title(event.getTitle())
                .description(event.getDescription())
                .ageLimit(event.getAge_limit())
                .build();
    }

    public Event toEvent(CreateEventRequest createEventRequest){
        return Event
                .builder()
                .title(createEventRequest.getTitle())
                .description(createEventRequest.getDescription())
                .age_limit(createEventRequest.getAgeLimit())
                .profile_id(createEventRequest.getProfileId())
                .build();
    }
    public List<EventDTO> toEventsDTO(List<Event> events){
        return events.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
