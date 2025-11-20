package com.jian.community.global.provider;

import com.jian.community.application.exception.ResourceNotFoundException;
import com.jian.community.domain.model.User;
import com.jian.community.domain.repository.crud.UserRepository;
import com.jian.community.global.exception.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetails loadUserByUserId(Long userId) throws UsernameNotFoundException {
        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.USER_NOT_EXISTS));

        return new CustomUserDetails(user);
    }
}
