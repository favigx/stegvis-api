package com.stegvis_api.stegvis_api.config.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.stegvis_api.stegvis_api.user.model.User;
import com.stegvis_api.stegvis_api.user.service.UserService;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.getUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return UserPrincipal.fromUser(user);
    }

    public UserDetails loadUserById(String id) {
        User user = userService.getUserById(id);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with id: " + id);
        }
        return UserPrincipal.fromUser(user);
    }
}
