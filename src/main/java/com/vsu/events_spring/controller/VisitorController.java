package com.vsu.events_spring.controller;

import com.vsu.events_spring.dto.response.VisitorDTO;
import com.vsu.events_spring.dto.request.CreateVisitorRequest;
import com.vsu.events_spring.service.VisitorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/visitors")
@AllArgsConstructor
public class VisitorController {
    private final VisitorService visitorService;

    @PostMapping
    public ResponseEntity<VisitorDTO> createVisitor(@Valid @RequestBody CreateVisitorRequest createVisitorRequest) {
        VisitorDTO visitorDTO = visitorService.createVisitor(createVisitorRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(visitorDTO);
    }
    @PutMapping("/{id}")
    public ResponseEntity<VisitorDTO> updateEndTimeVisitor(Long idVisitor) {
        VisitorDTO visitorDTO = visitorService.updateEndTimeVisitor(idVisitor);
        return ResponseEntity.status(HttpStatus.OK).body(visitorDTO);
    }
}
