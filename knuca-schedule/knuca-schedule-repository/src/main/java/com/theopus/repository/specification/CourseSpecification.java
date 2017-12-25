package com.theopus.repository.specification;

import com.theopus.entity.schedule.Circumstance;
import com.theopus.entity.schedule.Course;
import com.theopus.entity.schedule.Subject;
import com.theopus.entity.schedule.Teacher;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Collection;

import static com.theopus.repository.specification.SpecificationUtil.isCollectionContainsSomething;
import static com.theopus.repository.specification.SpecificationUtil.isCollectionEquals;

public class CourseSpecification {

    public static Specification<Course> sameCourse(Course course) {
        return ((root, query, cb) -> {
            Path<Object> subject = root.get("subject");
            Path<Object> type = root.get("type");
            query.distinct(true);
            Expression<Collection<Object>> expressionT = root.get("teachers");
            Predicate pl = cb.equal(subject, course.getSubject());
            Predicate pty = cb.equal(type, course.getType());

            return cb.and(pl, pty,
                    isCollectionEquals(expressionT, course.getTeachers(), cb));
        });
    }

    public static Specification withTeacher(Teacher teacher) {
        return ((root, query, cb) -> {
            query.distinct(true);
            Expression<Collection<Object>> expressionT = root.get("teachers");
            return cb.and(cb.isMember(teacher, expressionT));
        });
    }

    public static Specification withSubject(Subject subject) {
        return (root, query, cb) -> {
            query.distinct(true);
            Expression<Collection<Object>> expressionS = root.get("subject");
            return cb.equal(expressionS, subject);
        };
    }
}
