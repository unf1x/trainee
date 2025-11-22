package com.unfix.trainee.dto;

public record PullRequestShortDto(
        String pull_request_id,
        String pull_request_name,
        String author_id,
        String status
) {}
