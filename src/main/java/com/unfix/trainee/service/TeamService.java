package com.unfix.trainee.service;

import com.unfix.trainee.dto.TeamDto;

public interface TeamService {

    TeamDto createTeam(TeamDto request);

    TeamDto getTeam(String teamName);
}
