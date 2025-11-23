package com.unfix.trainee.dto;

public record UserReviewStatsDto(
        String user_id,
        String username,
        long review_count
) {}
