package com.theopus.repository.service.impl;

import com.google.common.collect.Sets;
import com.theopus.entity.schedule.*;
import com.theopus.repository.service.CurriculumService;
import com.theopus.repository.service.LessonService;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class LessonServiceImpl implements LessonService {

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
    public List<Lesson> getByTeacher(LocalDate date, Teacher teacher) {
        List<Curriculum> byGroup = curriculumService.getByTeacher(date, teacher);
        return byOneDay(byGroup, date);
    }


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
                                            System.out.println(lesson);
                                            System.out.println(lesson2);
                                            throw new RuntimeException("Date conflict");
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
