package com.theopus.repository.specification;

import com.theopus.entity.schedule.*;
import com.theopus.entity.schedule.enums.LessonOrder;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

public class CurriculumSpecification {

    public static Specification<Curriculum> sameCurriculum(Curriculum curriculum) {
        return ((root, query, cb) -> {
            Path<Object> course = root.get("course");
            Path<Object> group = root.get("group");

            query.distinct(true);

            Predicate cec = cb.equal(course, curriculum.getCourse());
            Predicate gec = cb.equal(group, curriculum.getGroup());
            return cb.and(cec, gec);
        });
    }

    public static Specification<Curriculum> withDateAndGroup(LocalDate date, Group group) {
        return (root, query, cb) -> {
            query.distinct(true);
            Path<Object> groupPath = root.get("group");
            Path<Object> circumstances = root.join("circumstances");
            Expression<Collection<Object>> dates = circumstances.get("dates");
            return cb.and(cb.equal(groupPath, group),
                    SpecificationUtil.isCollectionContainsSomething(dates, Collections.singleton(date),cb));
        };
    }

    public static Specification<Curriculum> atDateWithCourse(LocalDate date, Course course, LessonOrder order) {
        return (root, query, cb) -> {
            Path<Object> groupPath = root.get("group");
            Path<Object> circumstances = root.join("circumstances");
            Path<Object> order1 = circumstances.get("lessonOrder");
            Expression<Collection<Object>> dates = circumstances.get("dates");
            Path<Object> course1 = root.get("course");

            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Course> from = subquery.from(Course.class);

            return cb.and(cb.equal(course1, course), cb.equal(order1, order),
                    SpecificationUtil.isCollectionContainsSomething(dates, Collections.singleton(date),cb));
        };
    }

    public static Specification<Curriculum> withDateAndTeacher(LocalDate date, Teacher teacher) {
        return (root, query, cb) -> {
            query.distinct(true);
            Path<Object> course = root.join("course");
            Expression<Collection<Object>> teachers = course.get("teachers");


            Path<Object> circumstances = root.join("circumstances");
            Expression<Collection<Object>> dates = circumstances.get("dates");
            return cb.and(
                    SpecificationUtil.isCollectionContainsSomething(teachers, Collections.singleton(teacher), cb),
                    SpecificationUtil.isCollectionContainsSomething(dates, Collections.singleton(date),cb)
            );
        };
    }

    public static Specification<Curriculum> withDateAndRoom(LocalDate date, Room room) {
        return (root, query, cb) -> {
            query.distinct(true);

            Path<Object> circumstances = root.join("circumstances");
            Path<Object> roomCirc = circumstances.get("room");
            Expression<Collection<Object>> dates = circumstances.get("dates");
            return cb.and(
                    cb.equal(roomCirc,room),
                    SpecificationUtil.isCollectionContainsSomething(dates, Collections.singleton(date),cb)
            );
        };
    }
}
