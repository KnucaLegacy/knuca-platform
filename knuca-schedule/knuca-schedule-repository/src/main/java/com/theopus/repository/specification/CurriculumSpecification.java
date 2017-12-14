package com.theopus.repository.specification;

import com.theopus.entity.schedule.Curriculum;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

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
}
