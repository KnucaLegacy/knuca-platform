package com.theopus.repository.specification;

import com.theopus.entity.schedule.Circumstance;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class CircumstanceSpecification {

    public static Specification<Circumstance> sameCircumstance(Circumstance circumstance) {
        return (root, query, cb) -> {
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
}
