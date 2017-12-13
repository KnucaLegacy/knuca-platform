package com.theopus.repository.jparepo;

import com.theopus.entity.schedule.Curriculum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CurriculumRepository extends JpaRepository<Curriculum, Long>, JpaSpecificationExecutor {
}
