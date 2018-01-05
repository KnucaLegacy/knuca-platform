package com.theopus.restservice.controller;

import com.theopus.entity.schedule.Lesson;
import com.theopus.repository.service.GroupService;
import com.theopus.repository.service.LessonService;
import com.theopus.repository.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

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
    public List<Lesson> byGroup(@PathVariable Long id) {
        return service.getByGroup(LocalDate.now(), groupService.get(id));
    }

    @GetMapping("/{localDate}/group/{id}")
    public List<Lesson> byGroupAndDate(@PathVariable Long id, @PathVariable LocalDate localDate) {
        return service.getByGroup(localDate, groupService.get(id));
    }

    @GetMapping("/teacher/{id}")
    public List<Lesson> byTeacher(@PathVariable Long id) {
        return service.getByTeacher(LocalDate.now(), teacherService.get(id));
    }

    @GetMapping("/{localDate}/teacher/{id}")
    public List<Lesson> byTeacherAndDate(@PathVariable Long id, @PathVariable LocalDate localDate) {
        return service.getByTeacher(localDate, teacherService.get(id));
    }
}
