package com.stegvis_api.stegvis_api.user.controller;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.stegvis_api.stegvis_api.user.dto.UserLoginDTO;
import com.stegvis_api.stegvis_api.user.dto.UserLoginResponse;
import com.stegvis_api.stegvis_api.user.dto.UserRegistrationDTO;
import com.stegvis_api.stegvis_api.user.dto.UserRegistrationResponse;
import com.stegvis_api.stegvis_api.user.model.User;
import com.stegvis_api.stegvis_api.user.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponse> registerUser(
            @Valid @RequestBody UserRegistrationDTO userRegistrationDTO) {
        User user = userService.addUser(userRegistrationDTO);

        UserRegistrationResponse response = new UserRegistrationResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> loginUser(@Valid @RequestBody UserLoginDTO loginDTO) {
        UserLoginResponse response = userService.loginUser(loginDTO);
        return ResponseEntity.ok(response);
    }
}
