package com.unfix.trainee.dto;

public record BulkDeactivateResponse(
        String team_name,
        int deactivated_users,
        int updated_pull_requests
) {}
