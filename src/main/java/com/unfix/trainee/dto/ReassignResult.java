package com.unfix.trainee.dto;

public record ReassignResult(
        PullRequestDto pr,
        String replaced_by
) {}
