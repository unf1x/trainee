package com.unfix.trainee.impl;

import com.unfix.trainee.dto.PullRequestShortDto;
import com.unfix.trainee.dto.UserDto;
import com.unfix.trainee.dto.UserReviewsDto;
import com.unfix.trainee.exception.NotFoundException;
import com.unfix.trainee.model.PullRequestEntity;
import com.unfix.trainee.model.UserEntity;
import com.unfix.trainee.repository.PullRequestRepository;
import com.unfix.trainee.repository.UserRepository;
import com.unfix.trainee.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}
