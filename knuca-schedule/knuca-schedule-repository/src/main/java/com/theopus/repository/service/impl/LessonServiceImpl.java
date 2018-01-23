package com.theopus.repository.service.impl;

import com.google.common.collect.Sets;
import com.theopus.entity.schedule.*;
import com.theopus.repository.service.CurriculumService;
import com.theopus.repository.service.LessonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class LessonServiceImpl implements LessonService {

    private static final Logger LOG = LoggerFactory.getLogger(LessonServiceImpl.class);

    private CurriculumService curriculumService;

    public LessonServiceImpl(CurriculumService curriculumService) {
        this.curriculumService = curriculumService;
    }

    @Override
    public List<Lesson> getByGroup(LocalDate date, Group group) {
        List<Curriculum> byGroup = curriculumService.getByGroup(date, group);
        return byOneDay(byGroup, date);
    }

    @Override
    public Map<LocalDate, List<Lesson>> getByGroup(Set<LocalDate> date, Group group) {
        return new TreeMap<>(date.stream()
                .flatMap(localDate -> byOneDay(curriculumService.getByGroup(localDate, group), localDate).stream())
                .collect(Collectors.groupingBy(Lesson::getDate)));
    }

    @Override
    public List<Lesson> getByTeacher(LocalDate date, Teacher teacher) {
        List<Curriculum> byGroup = curriculumService.getByTeacher(date, teacher);
        return byOneDay(byGroup, date);
    }

    @Override
    public Map<LocalDate, List<Lesson>> getByTeacher(Set<LocalDate> date, Teacher teacher) {
        return new TreeMap<>(date.stream()
                .flatMap(localDate -> byOneDay(curriculumService.getByTeacher(localDate, teacher), localDate).stream())
                .collect(Collectors.groupingBy(Lesson::getDate)));
    }

    @Override
    public List<Lesson> getByRoom(LocalDate date, Room room) {
        List<Curriculum> byGroup = curriculumService.getByRoom(date, room);
        return byOneDay(byGroup, date);
    }

    @Override
    public Map<LocalDate, List<Lesson>> getByRoom(Set<LocalDate> dates, Room room) {
        return new TreeMap<>(dates.stream()
                .flatMap(localDate -> byOneDay(curriculumService.getByRoom(localDate, room), localDate).stream())
                .collect(Collectors.groupingBy(Lesson::getDate)));
    }

    //bullshit
    private List<Lesson> byOneDay(List<Curriculum> curriculumList, LocalDate localDate) {
        return curriculumList.stream()
                .flatMap(cur ->
                        cur.getCircumstances().stream()
                                .filter(cir ->
                                        cir.getDates().contains(localDate)
                                )
                )
                .collect(Collectors.groupingBy(Circumstance::getLessonOrder, Collectors.toList()))
                .entrySet().stream()
                .map(
                        pair -> {
                            pair.getValue().addAll(curriculumService.getWithCourseAtDate(localDate,
                                    pair.getValue().get(0).getCurriculum().getCourse(), pair.getKey()).stream().flatMap(cur ->
                                    cur.getCircumstances().stream()
                                            .filter(cir ->
                                                    cir.getDates().contains(localDate)
                                            )).collect(Collectors.toList()));
                            return pair.getValue().stream()
                                    .map(cir -> circumstanceToLesson(cir, localDate))
                                    .reduce((lesson, lesson2) -> {
                                        if (!Objects.equals(lesson.getCourse(), lesson2.getCourse())) {
                                            LOG.error("Conflict with: \n -{}; \n -{};", lesson, lesson2);
                                        }
                                        lesson.getGroups().addAll(lesson2.getGroups());
                                        lesson.getRooms().addAll(lesson2.getRooms());
                                        return lesson;
                                    }).get();
                        }
                )
                .sorted(Comparator.comparing(Lesson::getOrder))
                .collect(Collectors.toList());
    }

    private Lesson circumstanceToLesson(Circumstance circumstance, LocalDate localDate) {
        Lesson lesson = new Lesson();
        lesson.setCourse(circumstance.getCurriculum().getCourse());
        lesson.setGroups(Sets.newHashSet(circumstance.getCurriculum().getGroup()));
        lesson.setOrder(circumstance.getLessonOrder());
        lesson.setRooms(Sets.newHashSet(circumstance.getRoom()));
        lesson.setDate(localDate);
        return lesson;
    }
}
