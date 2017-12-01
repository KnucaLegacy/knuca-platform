package com.theopus.repository.specification;

import com.theopus.entity.schedule.Subject;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class SubjectSpecification {

    public static Specification<Subject> customerHasBirthday() {
        return new Specification<Subject>() {
            @Override
            public Predicate toPredicate(Root<Subject> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return null;
            }
        };
    }
}
