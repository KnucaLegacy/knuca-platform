package com.theopus.repository.specification;

import com.theopus.entity.schedule.Subject;
import com.theopus.repository.metamodel.Subject_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

public class SubjectSpecification {

    public static Specification<Subject> customerHasBirthday() {
        SingularAttribute<Subject, String> name = Subject_.name;
//        try {
//            Class.forName("com.theopus.repository.metamodel.Subject_");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        return new Specification<Subject>() {
            @Override
            public javax.persistence.criteria.Predicate toPredicate(Root<Subject> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return null;
            }
        };
    }

}
