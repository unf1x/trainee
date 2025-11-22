package com.unfix.trainee.dto;

import java.util.List;

public record TeamDto(
        String team_name,
        List<TeamMemberDto> members
) {}
