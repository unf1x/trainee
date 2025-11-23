package com.unfix.trainee.repository;


import com.unfix.trainee.model.PullRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PullRequestRepository extends JpaRepository<PullRequestEntity, String> {
    List<PullRequestEntity> findByReviewers_Id(String reviewerId);
    interface UserReviewStatProjection {
        String getUserId();
        String getUsername();
        long getReviewCount();
    }

    @Query("""
           select r.id as userId,
                  r.username as username,
                  count(pr.id) as reviewCount
           from PullRequestEntity pr
           join pr.reviewers r
           group by r.id, r.username
           """)
    List<UserReviewStatProjection> getReviewerStats();
}
