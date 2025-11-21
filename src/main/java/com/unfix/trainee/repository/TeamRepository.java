package com.unfix.trainee.repository;


import com.unfix.trainee.model.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<TeamEntity, String> {
}
