package com.theopus.repository.specification;

import com.theopus.entity.schedule.Circumstance;
import com.theopus.entity.schedule.Group;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Path;

public class CircumstanceSpecification {

    public static Specification<Circumstance> sameCircumstance(Circumstance circumstance) {
        return (root, query, cb) -> {
            query.distinct(true);
            Path<Object> curriculum = root.get("curriculum");
            Path<Object> lessonOrder = root.get("lessonOrder");
            Path<Object> room = root.get("room");
            return cb.and(
                    cb.equal(curriculum, circumstance.getCurriculum()),
                    cb.equal(lessonOrder, circumstance.getLessonOrder()),
                    cb.equal(room, circumstance.getRoom())
            );
        };
    }

    public static Specification<Circumstance> withGroup(Group group) {
        return (root, query, cb) -> {
            query.distinct(true);
            Path<Object> curriculumGroup = root.get("curriculum").get("group");
            return cb.equal(curriculumGroup, group);
        };
    }

}
