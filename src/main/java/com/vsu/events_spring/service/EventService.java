package com.vsu.events_spring.service;

import com.vsu.events_spring.dto.response.EventDTO;
import com.vsu.events_spring.entity.Event;
import com.vsu.events_spring.exception.EventNotFountException;
import com.vsu.events_spring.exception.ProfileNotFountException;
import com.vsu.events_spring.exception.UnauthorizedAccessException;
import com.vsu.events_spring.repository.EventRepository;
import com.vsu.events_spring.repository.ProfileRepository;
import com.vsu.events_spring.dto.request.CreateEventRequest;
import com.vsu.events_spring.dto.request.UpdateEventRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class EventService {
    private final ProfileRepository profileRepository;
    private final EventRepository eventRepository;
    private final EventMapperService eventMapperService;

    public EventDTO createEvent(CreateEventRequest createEventRequest) {

        if (profileRepository.findById(createEventRequest.getProfileId()) == null){
            throw new ProfileNotFountException("idProfile:" + createEventRequest.getProfileId());
        }
        Long id = eventRepository.create(eventMapperService.toEvent(createEventRequest));
        return eventMapperService.toDTO(createEventRequest, id);
    }
    public EventDTO getEventById(Long eventId) {
        if (eventRepository.findById(eventId) == null){
            throw new EventNotFountException("idEvent:" + eventId);
        }
        Event event = eventRepository.findById(eventId);
        return eventMapperService.toDTO(event);
    }

    public List<EventDTO> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return eventMapperService.toEventsDTO(events);
    }
    public EventDTO deleteEvent(Long eventId) {
        Event event = eventRepository.findById(eventId);
        if (event == null){
            throw new EventNotFountException("idEvent:" + eventId);
        }
        eventRepository.delete(eventId);
        return eventMapperService.toDTO(event);
    }
    public EventDTO updateEvent(Long eventId, UpdateEventRequest updateEventRequest) {
        Event event = eventRepository.findById(eventId);
        if (event == null){
            throw new EventNotFountException("idEvent:" + eventId);
        }
        if(!event.getProfile_id().equals(updateEventRequest.getProfileId())){
            throw new UnauthorizedAccessException("idEvent:" + eventId + " does not belong to profileId:" + updateEventRequest.getProfileId());
        }
        Event updatedEvent = Event.builder()
                .id_event(eventId)
                .title(updateEventRequest.getTitle())
                .description(updateEventRequest.getDescription())
                .age_limit((updateEventRequest.getAgeLimit()))
                .profile_id(updateEventRequest.getProfileId())
                .build();
        eventRepository.update(updatedEvent);
        return eventMapperService.toDTO(updatedEvent);
    }
    public List<EventDTO> getEventsByProfileId(Long profileId) {
        if (profileRepository.findById(profileId) == null){
            throw new ProfileNotFountException("idProfile:" + profileId);
        }
        List<Event> events = eventRepository.findByProfileId(profileId);
        return eventMapperService.toEventsDTO(events);
    }
    public List<EventDTO> getNotUsedEventsByProfileId(Long profileId) {
        if (profileRepository.findById(profileId) == null){
            throw new ProfileNotFountException("idProfile:" + profileId);
        }
        List<Event> events = eventRepository.findNotUsedByProfileId(profileId);
        return eventMapperService.toEventsDTO(events);
    }
    public EventDTO getActualEventByProfileId(Long profileId) {
        if (profileRepository.findById(profileId) == null){
            throw new ProfileNotFountException("idProfile:" + profileId);
        }
        Event event = eventRepository.findActualEventByProfileId(profileId);
        return eventMapperService.toDTO(event);
    }
    public EventDTO getEventByLightRoomId(Long lightRoomId) {
        Event event = eventRepository.findByLightRoomId(lightRoomId);
        return eventMapperService.toDTO(event);
    }

}
