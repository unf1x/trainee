package com.unfix.trainee.dto;

import java.util.List;

public record UserReviewsDto(
        String user_id,
        List<PullRequestShortDto> pull_requests
) {}
