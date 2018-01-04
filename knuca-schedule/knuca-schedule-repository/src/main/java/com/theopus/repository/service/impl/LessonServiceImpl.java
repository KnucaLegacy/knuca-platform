package com.theopus.repository.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.theopus.entity.schedule.*;
import com.theopus.repository.service.CurriculumService;
import com.theopus.repository.service.LessonService;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class LessonServiceImpl implements LessonService {

    private CurriculumService curriculumService;

    public LessonServiceImpl(CurriculumService curriculumService) {
        this.curriculumService = curriculumService;
    }

    @Override
    public List<Lesson> getByGroup(LocalDate date, Group group) {
        List<Curriculum> byGroup = curriculumService.getByGroup(date, group);
        System.out.println("---" + date);
        byOneDay(byGroup, date).forEach(System.out::println);
        return null;
    }

    @Override
    public List<Lesson> getByTeacher(LocalDate date, Teacher teacher) {
        return null;
    }


    private List<Lesson> byOneDay(List<Curriculum> curriculumList, LocalDate localDate) {
        List<Lesson> collect = curriculumList.stream()
                .flatMap(cur ->
                        cur.getCircumstances().stream()
                                .filter(cir ->
                                        cir.getDates().contains(localDate)
                                )
                )
                .map(cir -> {
                    Lesson lesson = new Lesson();
                    lesson.setGroups(Lists.newArrayList(cir.getCurriculum().getGroup()));
                    lesson.setOrder(cir.getLessonOrder());
                    lesson.setSubject(cir.getCurriculum().getCourse().getSubject());
                    lesson.setRoomList(Lists.newArrayList(cir.getRoom()));
                    lesson.setType(cir.getCurriculum().getCourse().getType());
                    lesson.setTeachers(Lists.newArrayList(cir.getCurriculum().getCourse().getTeachers()));
                    return lesson;
                })
                .collect(Collectors.toList());
        return collect;
    }
}
