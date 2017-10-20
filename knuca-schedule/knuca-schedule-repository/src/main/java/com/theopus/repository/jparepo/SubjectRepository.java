package com.theopus.repository.jparepo;

import com.theopus.entity.schedule.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SubjectRepository extends JpaRepository<Subject, Long>, JpaSpecificationExecutor {
}
