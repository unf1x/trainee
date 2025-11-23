package com.unfix.trainee.service;

import com.unfix.trainee.dto.UserReviewStatsDto;

import java.util.List;

public interface StatsService {
    List<UserReviewStatsDto> getAssignmentsByUser();
}
