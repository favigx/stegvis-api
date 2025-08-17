package com.stegvis_api.stegvis_api.user.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.security.Key;

import com.stegvis_api.stegvis_api.exception.type.AuthenticationException;
import com.stegvis_api.stegvis_api.exception.type.UserAlreadyExistsException;
import com.stegvis_api.stegvis_api.exception.type.UserNotFoundException;
import com.stegvis_api.stegvis_api.user.dto.UserLoginDTO;
import com.stegvis_api.stegvis_api.user.dto.UserLoginResponse;
import com.stegvis_api.stegvis_api.user.dto.UserRegistrationDTO;
import com.stegvis_api.stegvis_api.user.model.User;
import com.stegvis_api.stegvis_api.user.model.UserPreference;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class UserService {

    private final MongoOperations mongoOperations;
    private final PasswordEncoder passwordEncoder;

    public UserService(MongoOperations mongoOperations, PasswordEncoder passwordEncoder) {
        this.mongoOperations = mongoOperations;
        this.passwordEncoder = passwordEncoder;
    }

    @Value("${stegvis.jwt.secret}")
    private String jwtSecret;

    @Value("${stegvis.jwt.expiration-ms}")
    private int jwtExpirationMs;

    public User addUser(UserRegistrationDTO userRegistrationDTO) {

        User user = new User();

        if (getUserByEmail(userRegistrationDTO.getEmail()) != null) {
            throw new UserAlreadyExistsException("E-posten är redan kopplad till ett konto");
        }

        String encryptedPassword = passwordEncoder.encode(userRegistrationDTO.getPassword());

        user.setPassword(encryptedPassword);
        user.setEmail(userRegistrationDTO.getEmail());

        return mongoOperations.save(user);
    }

    public UserLoginResponse loginUser(UserLoginDTO userLoginDTO, HttpServletResponse response) {
        User user = getUserByEmail(userLoginDTO.getEmail());

        if (user == null || !passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            throw new AuthenticationException("Ogiltlig e-post eller lösenord");
        }

        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        String token = Jwts.builder()
                .setSubject(user.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) (jwtExpirationMs / 1000));
        response.setHeader("Set-Cookie", "jwt=" + token + "; Max-Age=" + (int) (jwtExpirationMs / 1000)
                + "; Path=/; HttpOnly; Secure; SameSite=Strict");
        response.addCookie(cookie);

        return new UserLoginResponse(user.getId(), user.getEmail());
    }

    public User setUserPreferences(String userId, UserPreference userPreference) {
        User user = getUserById(userId);

        if (user == null) {
            throw new UserNotFoundException("Användare med id " + userId + " hittade inte");
        }

        user.setUserPreference(userPreference);

        return mongoOperations.save(user);

    }

    public User getUserByEmail(String email) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        return mongoOperations.findOne(query, User.class);
    }

    public User getUserById(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(userId));
        return mongoOperations.findOne(query, User.class);
    }
}
