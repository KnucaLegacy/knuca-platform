package com.theopus.repository.specification;

import com.theopus.entity.schedule.Teacher;
import org.springframework.data.jpa.domain.Specification;

public class TeacherSpecification {

    public static Specification<Teacher> getByName(String name) {
        return (root, query, cb) -> cb.equal(root.get("name"), name);
    }
}
