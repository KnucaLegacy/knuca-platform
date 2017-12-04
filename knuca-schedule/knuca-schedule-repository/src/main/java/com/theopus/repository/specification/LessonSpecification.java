package com.theopus.repository.specification;

import com.theopus.entity.schedule.Circumstance;
import com.theopus.entity.schedule.Lesson;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Collection;

import static com.theopus.repository.specification.SpecificationUtil.isCollectionContainsSomething;
import static com.theopus.repository.specification.SpecificationUtil.isCollectionEquals;

public class LessonSpecification {

    public static Specification<Lesson> sameLesson(Lesson lesson) {
        return ((root, query, cb) -> {
            Path<Object> subject = root.get("subject");
            Path<Object> type = root.get("type");
            query.distinct(true);
            Expression<Collection<Object>> expressionT = root.get("teachers");
            Expression<Collection<Object>> expressionG = root.get("groups");
            Predicate pl = cb.equal(subject, lesson.getSubject());
            Predicate pty = cb.equal(type, lesson.getType());

            return cb.and(pl, pty,
                    isCollectionEquals(expressionT, lesson.getTeachers(), cb),
                    isCollectionEquals(expressionG, lesson.getGroups(), cb));
        });
    }

    public static Specification<Lesson> similarLesson(Lesson lesson) {
        return (root, query, cb) -> {
            Path<Object> subject = root.get("subject");
            Path<Object> type = root.get("type");
            query.distinct(true);
            Expression<Collection<Object>> expression = root.get("teachers");
            Predicate pl = cb.equal(subject, lesson.getSubject());
            Predicate pty = cb.equal(type, lesson.getType());

            Join<Object, Object> circumstances = root.join("circumstances");
            Predicate ci = anyCircumstance(circumstances, lesson.getCircumstances(), cb);

            return cb.and(pl, pty, ci, isCollectionEquals(expression, lesson.getTeachers(), cb));
        };
    }

    static Predicate anyCircumstance(Join<Object, Object> circumstaces, Collection<Circumstance> initial, CriteriaBuilder cb) {
        return cb.or(initial.stream()
                .map(circumstance -> cb.and(
                        cb.equal(circumstaces.get("lessonOrder"), circumstance.getLessonOrder()),
                        isCollectionContainsSomething(circumstaces.get("dates"), circumstance.getDates(), cb)
                )).toArray(Predicate[]::new));
    }

}
