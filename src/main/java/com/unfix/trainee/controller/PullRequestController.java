package com.unfix.trainee.controller;

import com.unfix.trainee.dto.*;
import com.unfix.trainee.service.PullRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PullRequestController {

    private final PullRequestService pullRequestService;

    @PostMapping("/pullRequest/create")
    public ResponseEntity<?> create(@RequestBody CreatePullRequestRequest request) {
        PullRequestDto pr = pullRequestService.createPullRequest(request);
        record ResponseWrapper(PullRequestDto pr) {}
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseWrapper(pr));
    }
    @PostMapping("/pullRequest/merge")
    public ResponseEntity<MergePullRequestResponse> merge(
            @RequestBody MergePullRequestRequest request
    ) {
        PullRequestDto pr = pullRequestService.merge(request.pull_request_id());
        return ResponseEntity.ok(new MergePullRequestResponse(pr));
    }

    @PostMapping("/pullRequest/reassign")
    public ResponseEntity<ReassignPullRequestResponse> reassign(
            @RequestBody ReassignPullRequestRequest request
    ) {
        ReassignResult result =
                pullRequestService.reassign(request.pull_request_id(), request.old_user_id());
        return ResponseEntity.ok(
                new ReassignPullRequestResponse(result.pr(), result.replaced_by())
        );
    }

}
