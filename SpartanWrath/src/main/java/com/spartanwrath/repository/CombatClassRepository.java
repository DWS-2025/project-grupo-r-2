package com.spartanwrath.repository;

import com.spartanwrath.model.CombatClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CombatClassRepository extends JpaRepository<CombatClass,Long> {



}
