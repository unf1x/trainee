package com.unfix.trainee.service;

import com.unfix.trainee.dto.UserDto;

public interface UserService {
    UserDto setIsActive(String userId, boolean isActive);
}
