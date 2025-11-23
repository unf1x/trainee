package com.unfix.trainee.controller;

import com.unfix.trainee.dto.*;
import com.unfix.trainee.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users/setIsActive")
    public ResponseEntity<?> setIsActive(@RequestBody UserSetIsActiveRequest request) {
        UserDto user = userService.setIsActive(
                request.user_id(),
                request.is_active()
        );

        record ResponseWrapper(UserDto user) {}

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseWrapper(user));
    }
    @GetMapping("/users/getReview")
    public ResponseEntity<UserReviewsDto> getReview(@RequestParam("user_id") String userId) {
        return ResponseEntity.ok(userService.getReviews(userId));
    }

    @PostMapping("/users/bulkDeactivate")
    public ResponseEntity<BulkDeactivateResponse> bulkDeactivate(
            @RequestBody BulkDeactivateRequest request
    ) {
        BulkDeactivateResponse resp = userService.bulkDeactivate(request.team_name());
        return ResponseEntity.ok(resp);
    }

}
