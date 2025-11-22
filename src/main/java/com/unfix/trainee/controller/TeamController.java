package com.unfix.trainee.controller;


import com.unfix.trainee.dto.TeamDto;
import com.unfix.trainee.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping("/team/add")
    public ResponseEntity<?> addTeam(@RequestBody TeamDto request) {
        TeamDto team = teamService.createTeam(request);
        // OpenAPI требует обёртку { "team": { ... } }
        record ResponseWrapper(TeamDto team) {}
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseWrapper(team));
    }

    @GetMapping("/team/get")
    public ResponseEntity<TeamDto> getTeam(@RequestParam("team_name") String teamName) {
        TeamDto team = teamService.getTeam(teamName);
        return ResponseEntity.ok(team);
    }
}

