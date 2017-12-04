package com.theopus.repository.specification;

import com.theopus.entity.schedule.Room;
import org.springframework.data.jpa.domain.Specification;

public class RoomSpecification {

    public static Specification<Room> getByName(String name) {
        return (root, query, cb) -> cb.equal(root.get("name"), name);
    }

}
