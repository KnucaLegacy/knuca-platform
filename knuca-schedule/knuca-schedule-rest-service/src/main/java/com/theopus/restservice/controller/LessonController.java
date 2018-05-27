package com.theopus.restservice.controller;

import com.theopus.entity.schedule.Group;
import com.theopus.entity.schedule.Lesson;
import com.theopus.entity.schedule.Room;
import com.theopus.entity.schedule.Teacher;
import com.theopus.repository.service.GroupService;
import com.theopus.repository.service.LessonService;
import com.theopus.repository.service.RoomService;
import com.theopus.repository.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/lessons")
public class LessonController {

    private LessonService service;
    private GroupService groupService;
    private TeacherService teacherService;
    private RoomService roomService;

    @Autowired
    public LessonController(LessonService service, GroupService groupService, TeacherService teacherService, RoomService roomService) {
        this.service = service;
        this.groupService = groupService;
        this.teacherService = teacherService;
        this.roomService = roomService;
    }

    @GetMapping("/group/{id}")
    public ResponseEntity<List<Lesson>> byGroup(@PathVariable Long id) {
        return ResponseEntity.ok(service.getByGroup(LocalDate.now(), group(id)));
    }

    @GetMapping("/{yyyy-MM-dd}/group/{id}")
    public ResponseEntity<List<Lesson>> byGroupAndDate(@PathVariable Long id, @PathVariable("yyyy-MM-dd") LocalDate localDate) {
        return ResponseEntity.ok(service.getByGroup(localDate, group(id)));
    }

    @GetMapping("/teacher/{id}")
    public ResponseEntity<List<Lesson>> byTeacher(@PathVariable Long id) {
        return ResponseEntity.ok(service.getByTeacher(LocalDate.now(), teacher(id)));
    }

    @GetMapping("/{yyyy-MM-dd}/teacher/{id}")
    public ResponseEntity<List<Lesson>> byTeacherAndDate(@PathVariable Long id, @PathVariable("yyyy-MM-dd") LocalDate localDate) {
        return ResponseEntity.ok(service.getByTeacher(localDate, teacher(id)));
    }

    @GetMapping("/room/{id}")
    public ResponseEntity<List<Lesson>> byRoom(@PathVariable Long id) {
        return ResponseEntity.ok(service.getByRoom(LocalDate.now(), room(id)));
    }

    @GetMapping("/{yyyy-MM-dd}/room/{id}")
    public ResponseEntity<List<Lesson>> byRoomAndDate(@PathVariable Long id, @PathVariable("yyyy-MM-dd") LocalDate localDate) {
        return ResponseEntity.ok(service.getByRoom(localDate, room(id)));
    }

    @GetMapping("/week/room/{id}")
    public ResponseEntity<Map<LocalDate, List<Lesson>>> byRoomAndWeek(@PathVariable Long id) {
        return ResponseEntity.ok(service.getByRoom(week(LocalDate.now()), room(id)));
    }

    @GetMapping("/week/group/{id}")
    public ResponseEntity<Map<LocalDate, List<Lesson>>> byGroupAndWeek(@PathVariable Long id) {
        return ResponseEntity.ok(service.getByGroup(week(LocalDate.now()), group(id)));
    }

    @GetMapping("/week/teacher/{id}")
    public ResponseEntity<Map<LocalDate, List<Lesson>>> byTeacherAndWeek(@PathVariable Long id) {
        return ResponseEntity.ok(service.getByTeacher(week(LocalDate.now()), teacher(id)));
    }

    @GetMapping("/week/{offset}/room/{id}")
    public ResponseEntity<Map<LocalDate, List<Lesson>>> byRoomAndWeekOffset(@PathVariable Integer offset,
                                                                            @PathVariable Long id) {
        return ResponseEntity.ok(service.getByRoom(week(LocalDate.now(), offset), room(id)));
    }

    @GetMapping("/week/{offset}/group/{id}")
    public ResponseEntity<Map<LocalDate, List<Lesson>>> byGroupAndDateOffset(@PathVariable Integer offset,
                                                                             @PathVariable Long id) {
        return ResponseEntity.ok(service.getByGroup(week(LocalDate.now(), offset), group(id)));
    }

    @GetMapping("/week/{offset}/teacher/{id}")
    public ResponseEntity<Map<LocalDate, List<Lesson>>> byTeacherAndWeekOffset(@PathVariable Integer offset,
                                                                               @PathVariable Long id) {
        return ResponseEntity.ok(service.getByTeacher(week(LocalDate.now(), offset), teacher(id)));
    }

    @GetMapping("/range/room/{id}")
    public ResponseEntity<Map<LocalDate, List<Lesson>>> byRoomAndRange(
            @RequestParam(name = "from", required = true) LocalDate from,
            @RequestParam(name = "fo", required = true) LocalDate to,
            @PathVariable Long id) {
        return ResponseEntity.ok(service.getByRoom(range(from, to), room(id)));
    }

    @GetMapping("/range/group/{id}")
    public ResponseEntity<Map<LocalDate, List<Lesson>>> byGroupAndRange(
            @RequestParam(name = "from", required = true) LocalDate from,
            @RequestParam(name = "fo", required = true) LocalDate to,
            @PathVariable Long id) {
        return ResponseEntity.ok(service.getByGroup(range(from, to), group(id)));
    }

    @GetMapping("/range/teacher/{id}")
    public ResponseEntity<Map<LocalDate, List<Lesson>>> byTeacherAndRange(
            @RequestParam(name = "from", required = true) LocalDate from,
            @RequestParam(name = "fo", required = true) LocalDate to,
            @PathVariable Long id) {
        return ResponseEntity.ok(service.getByTeacher(range(from, to), teacher(id)));
    }

    private Room room(Long id) {
        Room room = roomService.get(id);
        if (Objects.isNull(room)) {
            throw new EntityNotFoundException("Not found Room with id " + id);
        }
        return room;
    }

    private Teacher teacher(Long id) {
        Teacher teacher = teacherService.get(id);
        if (Objects.isNull(teacher)) {
            throw new EntityNotFoundException("Not found Teacher with id " + id);
        }
        return teacher;
    }

    private Group group(Long id) {
        Group group = groupService.get(id);
        if (Objects.isNull(group)) {
            throw new EntityNotFoundException("Not found Group with id " + id);
        }
        return group;
    }

    private static Set<LocalDate> week(LocalDate localDate) {
        return week(localDate, 0);
    }

    private static Set<LocalDate> week(LocalDate localDate, int offset) {
        LocalDate newDate = localDate.plusDays(7 * offset);
        return Arrays.stream(DayOfWeek.values()).map(newDate::with)
                .collect(Collectors.toSet());
    }


    private static Set<LocalDate> range(LocalDate from, LocalDate to) {
        final int days = (int) from.until(to, ChronoUnit.DAYS);
        return Stream.iterate(from, d -> d.plusDays(1))
                .limit(days).collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
