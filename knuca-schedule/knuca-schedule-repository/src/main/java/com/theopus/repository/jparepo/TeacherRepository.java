package com.theopus.repository.jparepo;

import com.theopus.entity.schedule.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TeacherRepository extends JpaRepository<Teacher, Long>, JpaSpecificationExecutor {
}
