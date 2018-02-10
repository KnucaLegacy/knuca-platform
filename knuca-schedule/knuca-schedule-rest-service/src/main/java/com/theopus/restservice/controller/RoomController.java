package com.theopus.restservice.controller;

import com.theopus.entity.schedule.Room;
import com.theopus.repository.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.Objects;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private RoomService service;

    @Autowired
    public RoomController(RoomService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Collection<Room>> all() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> byId(@PathVariable Long id) {
        Room room = service.get(id);
        if (Objects.isNull(room)) {
            throw new EntityNotFoundException("Not found Room with id " + id);
        }
        return ResponseEntity.ok(room);
    }
}
