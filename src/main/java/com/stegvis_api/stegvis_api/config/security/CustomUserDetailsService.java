package com.stegvis_api.stegvis_api.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.stegvis_api.stegvis_api.user.model.User;
import com.stegvis_api.stegvis_api.repository.UserRepository;

@RequiredArgsConstructor
@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Authentication attempt failed: no user found for provided email");
                    return new UsernameNotFoundException("User not found");
                });

        log.debug("Authentication attempt succeeded for userId={}", user.getId());
        return UserPrincipal.fromUser(user);
    }

    public UserDetails loadUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Authentication attempt failed: no user found for userId={}", id);
                    return new UsernameNotFoundException("User not found");
                });

        log.debug("Loaded user by userId={}", user.getId());
        return UserPrincipal.fromUser(user);
    }
}