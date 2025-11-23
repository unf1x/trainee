package com.unfix.trainee.impl;

import com.unfix.trainee.dto.UserReviewStatsDto;
import com.unfix.trainee.repository.PullRequestRepository;
import com.unfix.trainee.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final PullRequestRepository pullRequestRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserReviewStatsDto> getAssignmentsByUser() {
        return pullRequestRepository.getReviewerStats().stream()
                .map(p -> new UserReviewStatsDto(
                        p.getUserId(),
                        p.getUsername(),
                        p.getReviewCount()
                ))
                .toList();
    }
}
