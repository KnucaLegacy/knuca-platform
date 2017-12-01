package com.theopus.repository.specification;

import com.theopus.entity.schedule.Circumstance;
import com.theopus.entity.schedule.Lesson;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Iterator;

public class LessonSpecification {

    public static Specification<Lesson> similiarLesson(Lesson lesson) {
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

    public static Predicate isCollectionEquals(Expression<Collection<Object>> collection, Collection<?> initial, CriteriaBuilder cb){
        Predicate all = cb.and(initial.stream()
                .map(o -> cb.isMember(o, collection))
                .toArray(Predicate[]::new));
        Predicate size = cb.equal(cb.size(collection), initial.size());
        return cb.and(all, size);
    }

    public static Predicate isCollectionContainsSomething(Expression<Collection<Object>> collection, Collection<?> initial, CriteriaBuilder cb){
        if (initial.size() == 0)
            return cb.equal(cb.size(collection), initial.size());
        return cb.or(initial.stream()
                .map(o -> cb.isMember(o, collection))
                .toArray(Predicate[]::new));
    }

    static Predicate anyCircumstance(Join<Object, Object> circumstaces, Collection<Circumstance> initial, CriteriaBuilder cb){
        return cb.or(initial.stream()
                .map(circumstance -> cb.and(
                        cb.equal(circumstaces.get("lessonOrder"), circumstance.getLessonOrder()),
                        isCollectionContainsSomething(circumstaces.get("dates"), circumstance.getDates(), cb)
                )).toArray(Predicate[]::new));
    }


}
