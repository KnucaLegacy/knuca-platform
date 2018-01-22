package com.theopus.restservice.controller;

import com.theopus.entity.schedule.Lesson;
import com.theopus.repository.service.GroupService;
import com.theopus.repository.service.LessonService;
import com.theopus.repository.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/lessons")
public class LessonController {

    private LessonService service;
    private GroupService groupService;
    private TeacherService teacherService;

    @Autowired
    public LessonController(LessonService service, GroupService groupService, TeacherService teacherService) {
        this.service = service;
        this.groupService = groupService;
        this.teacherService = teacherService;
    }

    @GetMapping("/group/{id}")
    public ResponseEntity<List<Lesson>> byGroup(@PathVariable Long id) {
        return ResponseEntity.ok(service.getByGroup(LocalDate.now(), groupService.get(id)));
    }

    @GetMapping("/{yyyy-MM-dd}/group/{id}")
    public ResponseEntity<List<Lesson>> byGroupAndDate(@PathVariable Long id, @PathVariable("yyyy-MM-dd") LocalDate localDate) {
        return ResponseEntity.ok(service.getByGroup(localDate, groupService.get(id)));
    }

    @GetMapping("/week/group/{id}")
    public ResponseEntity<Map<LocalDate, List<Lesson>>> byGroupAndDate(@PathVariable Long id) {
        return ResponseEntity.ok(service.getByGroup(week(LocalDate.of(2017,9, 11)), groupService.get(id)));
    }

    @GetMapping("/teacher/{id}")
    public ResponseEntity<List<Lesson>> byTeacher(@PathVariable Long id) {
        return ResponseEntity.ok(service.getByTeacher(LocalDate.now(), teacherService.get(id)));
    }

    @GetMapping("/{yyyy-MM-dd}/teacher/{id}")
    public ResponseEntity<List<Lesson>> byTeacherAndDate(@PathVariable Long id, @PathVariable("yyyy-MM-dd") LocalDate localDate) {
        return ResponseEntity.ok(service.getByTeacher(localDate, teacherService.get(id)));
    }

    @GetMapping("/week/teacher/{id}")
    public ResponseEntity<Map<LocalDate, List<Lesson>>> byTeacherAndWeek(@PathVariable Long id) {
        return ResponseEntity.ok(service.getByTeacher(week(LocalDate.of(2017,9, 11)), teacherService.get(id)));
    }

    private static Set<LocalDate> week(LocalDate localDate) {
        return Arrays.stream(DayOfWeek.values()).map(localDate::with)
                .collect(Collectors.toSet());
    }
}
