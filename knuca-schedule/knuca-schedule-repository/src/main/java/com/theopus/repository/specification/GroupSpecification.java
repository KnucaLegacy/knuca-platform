package com.theopus.repository.specification;

import com.theopus.entity.schedule.Group;
import org.springframework.data.jpa.domain.Specification;

public class GroupSpecification {

    public static Specification<Group> getByName(String name) {
        return (root, query, cb) -> cb.equal(root.get("name"), name);
    }
}
