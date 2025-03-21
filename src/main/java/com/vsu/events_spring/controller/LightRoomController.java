package com.vsu.events_spring.controller;

import com.vsu.events_spring.dto.response.LightRoomDTO;
import com.vsu.events_spring.dto.request.CreateLightRoomRequest;
import com.vsu.events_spring.dto.request.GetPointsInAreaRequest;
import com.vsu.events_spring.service.LightRoomService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/lightroom")
@AllArgsConstructor
public class LightRoomController {
    private final LightRoomService lightRoomService;

    @PostMapping
    public ResponseEntity<LightRoomDTO> createLightRoom(@Valid @RequestBody CreateLightRoomRequest createLightRoomRequest) {
        LightRoomDTO createdLightRoom = lightRoomService.createNewLightRoom(createLightRoomRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLightRoom);
    }

    @GetMapping
    public ResponseEntity<List<LightRoomDTO>> getAllLightRooms() {
        List<LightRoomDTO> lightRoomDTOList = lightRoomService.getAllLightRooms();
        return ResponseEntity.status(HttpStatus.OK).body(lightRoomDTOList);
    }

    @GetMapping("/in-area")
    public ResponseEntity<List<LightRoomDTO>> getLightRoomsInArea(@Valid @ModelAttribute GetPointsInAreaRequest getPointsInAreaRequest) {
        List<LightRoomDTO> lightRoomDTOList = lightRoomService.getLightRoomsInArea(getPointsInAreaRequest);
        return ResponseEntity.status(HttpStatus.OK).body(lightRoomDTOList);
    }
}
