package com.unfix.trainee.dto;

public record ReassignPullRequestRequest(
        String pull_request_id,
        String old_user_id
) {}
