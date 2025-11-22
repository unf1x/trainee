package com.unfix.trainee.dto;

import java.time.Instant;
import java.util.List;

public record PullRequestDto(
        String pull_request_id,
        String pull_request_name,
        String author_id,
        String status,
        List<String> assigned_reviewers,
        Instant createdAt,
        Instant mergedAt
) {}
