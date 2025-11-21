package com.unfix.trainee.repository;


import com.unfix.trainee.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, String> {

    List<UserEntity> findByTeam_Name(String teamName);
}
