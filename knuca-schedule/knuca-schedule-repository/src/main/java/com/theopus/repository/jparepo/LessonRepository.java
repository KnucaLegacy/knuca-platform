package com.theopus.repository.jparepo;

import com.theopus.entity.schedule.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LessonRepository extends JpaRepository<Lesson, Long>, JpaSpecificationExecutor {
}
