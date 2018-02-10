package com.theopus.restservice.controller;

import com.theopus.entity.schedule.Teacher;
import com.theopus.repository.service.TeacherService;
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
@RequestMapping("/teachers")
public class TeacherController {

    private TeacherService service;

    @Autowired
    public TeacherController(TeacherService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Collection<Teacher>> all() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Teacher> byId(@PathVariable Long id) {
        Teacher teacher = service.get(id);
        if (Objects.isNull(teacher)) {
            throw new EntityNotFoundException("Not found Teacher with id " + id);
        }
        return ResponseEntity.ok(teacher);
    }
}
