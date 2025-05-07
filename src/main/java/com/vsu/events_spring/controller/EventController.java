package com.vsu.events_spring.controller;

import com.vsu.events_spring.dto.response.EventDTO;
import com.vsu.events_spring.dto.request.CreateEventRequest;
import com.vsu.events_spring.dto.request.UpdateEventRequest;
import com.vsu.events_spring.dto.response.LastAttendedEventResponse;
import com.vsu.events_spring.service.EventService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/events")
@AllArgsConstructor
public class EventController {
    private final EventService eventService;
    @Autowired
    private HttpServletRequest request;

    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@Valid @RequestBody CreateEventRequest eventRequest) {
        EventDTO createdEvent = eventService.createEvent(eventRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable(value = "id") Long id) {
        EventDTO eventDTO = eventService.getEventById(id);
        return ResponseEntity.status(HttpStatus.OK).body(eventDTO);
    }

    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        List<EventDTO> eventDTOList = eventService.getAllEvents();
        return ResponseEntity.status(HttpStatus.OK).body(eventDTOList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EventDTO> deleteEvent(@PathVariable(value = "id") Long id) {
        EventDTO eventDTO = eventService.deleteEvent(id);
        return ResponseEntity.status(HttpStatus.OK).body(eventDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable(value = "id") Long id, @Valid @RequestBody UpdateEventRequest updateEventRequest) {
        EventDTO eventDTO = eventService.updateEvent(id, updateEventRequest);
        return ResponseEntity.status(HttpStatus.OK).body(eventDTO);
    }

    @GetMapping("/profile/{profileId}")
    public ResponseEntity<List<EventDTO>> getEventByProfileId(@PathVariable(value = "profileId") Long profileId) {
        List<EventDTO> eventDTOList = eventService.getEventsByProfileId(profileId);
        return ResponseEntity.status(HttpStatus.OK).body(eventDTOList);
    }
    @GetMapping(value = "/profile/{profileId}", params = "type=not-used")
    public ResponseEntity<List<EventDTO>> getNotUsedEventsByProfileId(
            @PathVariable(value = "profileId") Long profileId) {
        List<EventDTO> eventDTOList = eventService.getNotUsedEventsByProfileId(profileId);
        return ResponseEntity.status(HttpStatus.OK).body(eventDTOList);
    }
    @GetMapping(value = "/profile/{profileId}", params = "type=actual-event")
    public ResponseEntity<EventDTO> getActualEventByProfileId(
            @PathVariable(value = "profileId") Long profileId) {
        EventDTO eventDTO = eventService.getActualEventByProfileId(profileId);
        return ResponseEntity.status(HttpStatus.OK).body(eventDTO);
    }
    @GetMapping(value = "/profile/{profileId}", params = "type=last-events")
    public ResponseEntity<List<LastAttendedEventResponse>> getTheLastAttendedEvents(
            @PathVariable(value = "profileId") Long profileId) {
        String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "") + "/api/images";
        List<LastAttendedEventResponse> events = eventService.getTheLastAttendedEvents(profileId, baseUrl);
        return ResponseEntity.status(HttpStatus.OK).body(events);
    }

    @GetMapping("/lightRoom/{lightRoomId}")
    public ResponseEntity<EventDTO> getEventByLightRoomId(@PathVariable(value = "lightRoomId") Long lightRoomId) {
        EventDTO event = eventService.getEventByLightRoomId(lightRoomId);
        return ResponseEntity.status(HttpStatus.OK).body(event);
    }
}
