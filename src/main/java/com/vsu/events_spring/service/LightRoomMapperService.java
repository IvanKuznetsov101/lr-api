package com.vsu.events_spring.service;

import com.vsu.events_spring.dto.response.LightRoomDTO;
import com.vsu.events_spring.entity.LightRoom;
import com.vsu.events_spring.dto.request.CreateLightRoomRequest;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKBReader;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LightRoomMapperService {
    public LightRoomDTO toDTO(CreateLightRoomRequest createLightRoomRequest, Long id) {
        return LightRoomDTO
                .builder()
                .id(id)
                .latitude(createLightRoomRequest.getLatitude())
                .longitude(createLightRoomRequest.getLongitude())
                .build();
    }

    public LightRoomDTO toDTO(LightRoom lightRoom, Long id) {
        try {
            WKBReader reader = new WKBReader();
            Geometry geometry = reader.read(lightRoom.getLight_room_coordinates().getBytes());
            return LightRoomDTO
                    .builder()
                    .id(id)
                    .latitude(geometry.getCoordinate().getX())
                    .longitude(geometry.getCoordinate().getY())
                    .startTime(lightRoom.getLight_room_start_time())
                    .endTime(lightRoom.getLight_room_end_time())
                    .build();
        } catch (ParseException e) {
            return null;
        }
    }

    public LightRoomDTO toDTO(LightRoom lightRoom) {
        try {
            GeometryFactory gm = new GeometryFactory(new PrecisionModel(), 4326);
            WKBReader wkbr = new WKBReader(gm);
            byte[] wkbBytes = WKBReader.hexToBytes(lightRoom.getLight_room_coordinates());
            final Geometry geometry = wkbr.read(wkbBytes);
            return LightRoomDTO
                    .builder()
                    .id(lightRoom.getId_light_room())
                    .latitude(geometry.getCoordinate().getY())
                    .longitude(geometry.getCoordinate().getX())
                    .startTime(lightRoom.getLight_room_start_time())
                    .endTime(lightRoom.getLight_room_end_time())
                    .build();
        } catch (ParseException e) {
            return null;
        }
    }

    public List<LightRoomDTO> toLightRoomsDTO(List<LightRoom> lightRooms) {
        return lightRooms.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
