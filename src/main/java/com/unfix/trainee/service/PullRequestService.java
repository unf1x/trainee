package com.unfix.trainee.service;

import com.unfix.trainee.dto.CreatePullRequestRequest;
import com.unfix.trainee.dto.PullRequestDto;
import com.unfix.trainee.dto.ReassignResult;

public interface PullRequestService {

    PullRequestDto createPullRequest(CreatePullRequestRequest request);

    PullRequestDto merge(String pullRequestId);

    ReassignResult reassign(String pullRequestId, String oldUserId);
}
