package com.unfix.trainee.impl;

import com.unfix.trainee.dto.UserDto;
import com.unfix.trainee.exception.NotFoundException;
import com.unfix.trainee.model.UserEntity;
import com.unfix.trainee.repository.UserRepository;
import com.unfix.trainee.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

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
}
