package com.theopus.repository.specification;

import com.theopus.entity.schedule.Course;
import com.theopus.entity.schedule.Curriculum;
import com.theopus.entity.schedule.Group;
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

//    public static Specification<Curriculum> withDateAndCourse(LocalDate date, Course course) {
//        return (root, query, cb) -> {
//            Path<Object> groupPath = root.get("group");
//            Path<Object> circumstances = root.join("circumstances");
//            Expression<Collection<Object>> dates = circumstances.get("dates");
//            return cb.and(CourseSpecification.sameCourse(course).toPredicate(query.from(), ),
//                    SpecificationUtil.isCollectionContainsSomething(dates, Collections.singleton(date),cb));
//        };
//    }
}
