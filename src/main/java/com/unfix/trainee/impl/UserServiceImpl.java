package com.unfix.trainee.impl;

import com.unfix.trainee.dto.BulkDeactivateResponse;
import com.unfix.trainee.dto.PullRequestShortDto;
import com.unfix.trainee.dto.UserDto;
import com.unfix.trainee.dto.UserReviewsDto;
import com.unfix.trainee.exception.NotFoundException;
import com.unfix.trainee.model.PullRequestEntity;
import com.unfix.trainee.model.PullRequestStatus;
import com.unfix.trainee.model.UserEntity;
import com.unfix.trainee.repository.PullRequestRepository;
import com.unfix.trainee.repository.UserRepository;
import com.unfix.trainee.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PullRequestRepository pullRequestRepository;

    @Override
    @Transactional
    public UserDto setIsActive(String userId, boolean isActive) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found"));

        user.setActive(isActive);
        userRepository.save(user);

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getTeam().getName(),
                user.isActive()
        );
    }
    @Override
    @Transactional(readOnly = true)
    public UserReviewsDto getReviews(String userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found"));

        List<PullRequestEntity> prs = pullRequestRepository.findByReviewers_Id(userId);

        List<PullRequestShortDto> shortDtos = prs.stream()
                .map(pr -> new PullRequestShortDto(
                        pr.getId(),
                        pr.getName(),
                        pr.getAuthor().getId(),
                        pr.getStatus().name()
                ))
                .toList();

        return new UserReviewsDto(user.getId(), shortDtos);
    }
    @Override
    @Transactional
    public BulkDeactivateResponse bulkDeactivate(String teamName) {
        // 1. Найти всех юзеров команды
        List<UserEntity> users = userRepository.findByTeam_Name(teamName);
        if (users.isEmpty()) {
            throw new NotFoundException("team not found");
        }

        int deactivatedCount = 0;
        int updatedPrCount = 0;

        // 2. Деактивируем всех
        for (UserEntity user : users) {
            if (user.isActive()) {
                user.setActive(false);
                deactivatedCount++;
            }
        }
        userRepository.saveAll(users);

        for (UserEntity user : users) {
            List<PullRequestEntity> prs = pullRequestRepository.findByReviewers_Id(user.getId());

            for (PullRequestEntity pr : prs) {
                if (pr.getStatus() != PullRequestStatus.OPEN) {
                    continue;
                }

                if (!pr.getReviewers().contains(user)) {
                    continue;
                }

                // поиск кандидата
                UserEntity replacement = findReplacementForBulk(pr, user);

                if (replacement != null) {
                    pr.getReviewers().remove(user);
                    pr.getReviewers().add(replacement);
                } else {
                    pr.getReviewers().remove(user);
                }

                updatedPrCount++;
            }
        }


        return new BulkDeactivateResponse(teamName, deactivatedCount, updatedPrCount);
    }
    private UserEntity findReplacementForBulk(PullRequestEntity pr, UserEntity oldReviewer) {
        var team = oldReviewer.getTeam();
        if (team == null) {
            return null;
        }

        List<UserEntity> teamMembers = userRepository.findByTeam_Name(team.getName());

        List<UserEntity> candidates = new ArrayList<>();
        for (UserEntity u : teamMembers) {
            if (u.isActive()
                    && !u.getId().equals(oldReviewer.getId())
                    && !u.getId().equals(pr.getAuthor().getId())
                    && !pr.getReviewers().contains(u)) {
                candidates.add(u);
            }
        }

        if (candidates.isEmpty()) {
            return null;
        }

        return candidates.get(ThreadLocalRandom.current().nextInt(candidates.size()));
    }
}
