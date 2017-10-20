package com.theopus.repository.jparepo;

import com.theopus.entity.schedule.Circumstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CircumstanceRepository extends JpaRepository<Circumstance, Long>, JpaSpecificationExecutor {
}
