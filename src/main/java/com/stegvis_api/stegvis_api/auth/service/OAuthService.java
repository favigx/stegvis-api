package com.stegvis_api.stegvis_api.auth.service;

import com.stegvis_api.stegvis_api.user.model.OathCredentials;
import com.stegvis_api.stegvis_api.user.model.User;
import com.stegvis_api.stegvis_api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final UserRepository userRepository;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User linkOAuthToUser(User user, String provider, String oauthId, Set<String> scopes) {
        if (!user.isGoogleLinked() && provider.equals("google")) {
            user.setGoogleLinked(true);
            user.setGoogleOathCredentials(buildOathCredentials(provider, oauthId, scopes));
            return userRepository.save(user);
        }
        return user;
    }

    public User createNewOAuthUser(String email, String provider, String oauthId, Set<String> scopes, String firstname,
            String lastname) {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setFirstname(firstname);
        newUser.setLastname(lastname);

        if (provider.equals("google")) {
            newUser.setGoogleLinked(true);
            newUser.setGoogleOathCredentials(buildOathCredentials(provider, oauthId, scopes));
        }

        return userRepository.save(newUser);
    }

    public OathCredentials buildOathCredentials(String provider, String oauthId, Set<String> scopes) {
        return OathCredentials.builder()
                .provider(provider)
                .oathId(oauthId)
                .scopes(scopes)
                .build();
    }
}