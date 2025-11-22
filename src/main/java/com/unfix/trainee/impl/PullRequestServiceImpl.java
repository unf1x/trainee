package com.unfix.trainee.impl;

import com.unfix.trainee.dto.CreatePullRequestRequest;
import com.unfix.trainee.dto.PullRequestDto;
import com.unfix.trainee.dto.ReassignResult;
import com.unfix.trainee.exception.*;
import com.unfix.trainee.model.PullRequestEntity;
import com.unfix.trainee.model.PullRequestStatus;
import com.unfix.trainee.model.UserEntity;
import com.unfix.trainee.repository.PullRequestRepository;
import com.unfix.trainee.repository.UserRepository;
import com.unfix.trainee.service.PullRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class PullRequestServiceImpl implements PullRequestService {

    private final PullRequestRepository pullRequestRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public PullRequestDto createPullRequest(CreatePullRequestRequest request) {
        String prId = request.pull_request_id();

        if (pullRequestRepository.existsById(prId)) {
            throw new PrExistsException("PR id already exists");
        }

        UserEntity author = userRepository.findById(request.author_id())
                .orElseThrow(() -> new NotFoundException("author not found"));

        if (author.getTeam() == null) {
            throw new NotFoundException("author team not found");
        }

        List<UserEntity> teamMembers = userRepository.findByTeam_Name(author.getTeam().getName());

        List<UserEntity> candidates = new ArrayList<>();
        for (UserEntity u : teamMembers) {
            if (u.isActive() && !u.getId().equals(author.getId())) {
                candidates.add(u);
            }
        }

        Collections.shuffle(candidates);
        List<UserEntity> reviewers = candidates.stream()
                .limit(2)
                .toList();

        PullRequestEntity pr = new PullRequestEntity();
        pr.setId(prId);
        pr.setName(request.pull_request_name());
        pr.setAuthor(author);
        pr.setStatus(PullRequestStatus.OPEN);
        pr.setCreatedAt(Instant.now());
        pr.getReviewers().addAll(reviewers);

        pullRequestRepository.save(pr);

        return toDto(pr);
    }

    private PullRequestDto toDto(PullRequestEntity pr) {
        List<String> reviewerIds = pr.getReviewers().stream()
                .map(UserEntity::getId)
                .toList();

        return new PullRequestDto(
                pr.getId(),
                pr.getName(),
                pr.getAuthor().getId(),
                pr.getStatus().name(),
                reviewerIds,
                pr.getCreatedAt(),
                pr.getMergedAt()
        );
    }
    @Override
    @Transactional
    public PullRequestDto merge(String pullRequestId) {
        PullRequestEntity pr = pullRequestRepository.findById(pullRequestId)
                .orElseThrow(() -> new NotFoundException("PR not found"));

        if (pr.getStatus() == PullRequestStatus.OPEN) {
            pr.setStatus(PullRequestStatus.MERGED);
            if (pr.getMergedAt() == null) {
                pr.setMergedAt(Instant.now());
            }
            pullRequestRepository.save(pr);
        }

        return toDto(pr);
    }

    @Override
    @Transactional
    public ReassignResult reassign(String pullRequestId, String oldUserId) {
        PullRequestEntity pr = pullRequestRepository.findById(pullRequestId)
                .orElseThrow(() -> new NotFoundException("PR not found"));

        UserEntity oldReviewer = userRepository.findById(oldUserId)
                .orElseThrow(() -> new NotFoundException("user not found"));

        if (pr.getStatus() == PullRequestStatus.MERGED) {
            throw new PrMergedException("cannot reassign on merged PR");
        }

        if (!pr.getReviewers().contains(oldReviewer)) {
            throw new NotAssignedException("reviewer is not assigned to this PR");
        }

        if (oldReviewer.getTeam() == null) {
            throw new NoCandidateException("no active replacement candidate in team");
        }

        var teamName = oldReviewer.getTeam().getName();
        var teamMembers = userRepository.findByTeam_Name(teamName);

        List<UserEntity> candidates = new ArrayList<>();
        for (UserEntity u : teamMembers) {
            if (u.isActive()
                    && !u.getId().equals(oldReviewer.getId())
                    && !pr.getReviewers().contains(u)) {
                candidates.add(u);
            }
        }

        if (candidates.isEmpty()) {
            throw new NoCandidateException("no active replacement candidate in team");
        }

        UserEntity replacement = candidates.get(
                ThreadLocalRandom.current().nextInt(candidates.size())
        );

        pr.getReviewers().remove(oldReviewer);
        pr.getReviewers().add(replacement);
        pullRequestRepository.save(pr);

        return new ReassignResult(toDto(pr), replacement.getId());
    }

}
