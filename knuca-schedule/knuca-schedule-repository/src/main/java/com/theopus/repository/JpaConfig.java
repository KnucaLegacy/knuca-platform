package com.theopus.repository;


import com.theopus.entity.schedule.*;
import com.theopus.entity.schedule.enums.LessonOrder;
import com.theopus.entity.schedule.enums.LessonType;
import com.theopus.repository.jparepo.LessonRepository;
import com.theopus.repository.jparepo.SubjectRepository;
import com.theopus.repository.jparepo.TeacherRepository;
import com.theopus.repository.specification.LessonSpecification;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//TODO: to remove
@SpringBootApplication
@EntityScan("com.theopus.entity.schedule")
public class JpaConfig {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(JpaConfig.class);

        LessonRepository lessonRepository = run.getBean(LessonRepository.class);
        TeacherRepository teacherRepository = run.getBean(TeacherRepository.class);
        SubjectRepository subjectRepository = run.getBean(SubjectRepository.class);
        Teacher teacher = new Teacher();
        teacher.setName("Loh");
        Teacher save = teacherRepository.save(teacher);



        List<Teacher> set = new ArrayList<>();
        set.add(save);



        Lesson lesson = new Lesson();
        Subject subject = new Subject();
        subject.setName("lol");
        lesson.setSubject(subjectRepository.save(subject));
        lesson.setType(LessonType.LAB);
        lesson.setTeachers(set);
        lesson.setCircumstances(cir(lesson));
        lessonRepository.save(lesson);


        System.out.println("All");
        System.out.println(lessonRepository.findAll());
        System.out.println("Same");
        Lesson lesson1 = new Lesson();
        Subject subjectkek = subjectRepository.findOne(1L);
        lesson1.setSubject(subjectkek);
        lesson1.setType(LessonType.LAB);
        List<Teacher> set2 = new ArrayList<>();
        set2.add(save);

        lesson1.setTeachers(set2);
        lesson1.setCircumstances(cir(lesson1));
//        lessonRepository.save(lesson1);
        System.out.println(lesson1);
        System.out.println("ALL-SAVED");
        System.out.println(lessonRepository.findAll());
        System.out.println("_________");
        System.out.println(lessonRepository.findAll(LessonSpecification.similarLesson(lesson1)));
    }

    static Set<Circumstance> cir(Lesson lesson){
        Set<Circumstance> circumstances = new HashSet<>();
        Circumstance circumstance = new Circumstance();
        circumstance.setLesson(lesson);
        circumstance.setLessonOrder(LessonOrder.FIFTH);

        Set<LocalDate> localDates = new HashSet<>();
        circumstance.setDates(localDates);

        Set<Room> rooms = new HashSet<>();
        circumstance.setRooms(rooms);
        circumstances.add(circumstance);
        return circumstances;
    }

}
