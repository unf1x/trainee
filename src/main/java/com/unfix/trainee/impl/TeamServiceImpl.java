package com.unfix.trainee.impl;


import com.unfix.trainee.dto.TeamDto;
import com.unfix.trainee.dto.TeamMemberDto;

import com.unfix.trainee.exception.NotFoundException;
import com.unfix.trainee.exception.TeamExistsException;
import com.unfix.trainee.model.TeamEntity;
import com.unfix.trainee.model.UserEntity;
import com.unfix.trainee.repository.TeamRepository;
import com.unfix.trainee.repository.UserRepository;
import com.unfix.trainee.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public TeamDto createTeam(TeamDto request) {
        String teamName = request.team_name();

        // Если команда уже существует - 400 TEAM_EXISTS (по OpenAPI)
        if (teamRepository.existsById(teamName)) {
            throw new TeamExistsException("team_name already exists");
        }

        // Создаём новую команду
        TeamEntity team = new TeamEntity();
        team.setName(teamName);
        teamRepository.save(team);

        // Создаём/обновляем пользователей
        for (TeamMemberDto memberDto : request.members()) {
            UserEntity user = userRepository.findById(memberDto.user_id())
                    .orElseGet(UserEntity::new);

            user.setId(memberDto.user_id());
            user.setUsername(memberDto.username());
            user.setActive(memberDto.is_active());
            user.setTeam(team);

            userRepository.save(user);
        }

        // Загружаем членов команды обратно из БД и маппим в DTO
        List<UserEntity> members = userRepository.findByTeam_Name(teamName);
        return toTeamDto(team, members);
    }

    @Override
    @Transactional(readOnly = true)
    public TeamDto getTeam(String teamName) {
        TeamEntity team = teamRepository.findById(teamName)
                .orElseThrow(() -> new NotFoundException("team not found"));

        List<UserEntity> members = userRepository.findByTeam_Name(teamName);
        return toTeamDto(team, members);
    }

    private TeamDto toTeamDto(TeamEntity team, List<UserEntity> members) {
        List<TeamMemberDto> memberDtos = members.stream()
                .map(u -> new TeamMemberDto(
                        u.getId(),
                        u.getUsername(),
                        u.isActive()
                ))
                .toList();

        return new TeamDto(team.getName(), memberDtos);
    }
}
