package com.unfix.trainee.dto;

public record UserDto(
        String user_id,
        String username,
        String team_name,
        boolean is_active
) {}
