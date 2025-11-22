package com.unfix.trainee.service;

import com.unfix.trainee.dto.TeamDto;
import com.unfix.trainee.dto.TeamMemberDto;
import com.unfix.trainee.exception.TeamExistsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TeamServiceImplTest {

    @Autowired
    private TeamService teamService;

    @Test
    void testCreateAndGetTeam() {
        TeamDto request = new TeamDto(
                "backend",
                List.of(
                        new TeamMemberDto("u1", "Alice", true),
                        new TeamMemberDto("u2", "Bob", true)
                )
        );

        TeamDto created = teamService.createTeam(request);
        assertEquals("backend", created.team_name());
        assertEquals(2, created.members().size());

        TeamDto loaded = teamService.getTeam("backend");
        assertEquals("backend", loaded.team_name());
        assertEquals(2, loaded.members().size());
    }

    @Test
    void testTeamExists() {
        TeamDto request = new TeamDto(
                "duplicate",
                List.of(new TeamMemberDto("u1", "A", true))
        );

        teamService.createTeam(request);

        assertThrows(
                TeamExistsException.class,
                () -> teamService.createTeam(request)
        );
    }
}
