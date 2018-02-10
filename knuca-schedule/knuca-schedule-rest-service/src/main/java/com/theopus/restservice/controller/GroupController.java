package com.theopus.restservice.controller;

import com.theopus.entity.schedule.Group;
import com.theopus.repository.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/groups")
public class GroupController {

    private static final Logger LOG = LoggerFactory.getLogger(GroupController.class);
    private GroupService service;

    @Autowired
    public GroupController(GroupService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Collection<Group>> all() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Group> byId(@PathVariable Long id) {
        Group group = service.get(id);
        if (Objects.isNull(group)) {
            throw new EntityNotFoundException("Not found Group with id " + id);
        }
        return ResponseEntity.ok(group);
    }
}
