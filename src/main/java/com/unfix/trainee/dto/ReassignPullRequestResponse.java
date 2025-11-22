package com.unfix.trainee.dto;

public record ReassignPullRequestResponse(
        PullRequestDto pr,
        String replaced_by
) {}
