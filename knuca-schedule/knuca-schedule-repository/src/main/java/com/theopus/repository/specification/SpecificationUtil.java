package com.theopus.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.util.Collection;

public class SpecificationUtil {


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

}
