package com.theopus.repository.jparepo;

import com.theopus.entity.schedule.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor {
}
