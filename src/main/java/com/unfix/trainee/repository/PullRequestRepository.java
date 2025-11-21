package com.unfix.trainee.repository;


import com.unfix.trainee.model.PullRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PullRequestRepository extends JpaRepository<PullRequestEntity, String> {

    @Query("""
           select pr
           from PullRequestEntity pr
           join pr.reviewers r
           where r.id = :userId
           """)
    List<PullRequestEntity> findByReviewerId(@Param("userId") String userId);
}
