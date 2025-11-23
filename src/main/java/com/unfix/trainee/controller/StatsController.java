package com.unfix.trainee.controller;

import com.unfix.trainee.dto.UserReviewStatsDto;
import com.unfix.trainee.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/stats/assignments/byUser")
    public List<UserReviewStatsDto> getAssignmentsByUser() {
        return statsService.getAssignmentsByUser();
    }
}
