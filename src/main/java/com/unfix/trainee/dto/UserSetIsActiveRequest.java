package com.unfix.trainee.dto;

public record UserSetIsActiveRequest(
        String user_id,
        boolean is_active
) {}
