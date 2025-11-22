package com.unfix.trainee.service;

import com.unfix.trainee.dto.UserDto;
import com.unfix.trainee.dto.UserReviewsDto;

public interface UserService {
    UserDto setIsActive(String userId, boolean isActive);

    UserReviewsDto getReviews(String userId);
}

