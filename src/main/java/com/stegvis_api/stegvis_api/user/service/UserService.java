package com.stegvis_api.stegvis_api.user.service;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.stegvis_api.stegvis_api.exception.type.UserNotFoundException;
import com.stegvis_api.stegvis_api.user.model.User;
import com.stegvis_api.stegvis_api.user.model.UserPreference;

@Service
public class UserService {

    private final MongoOperations mongoOperations;

    public UserService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public User setUserPreferences(String userId, UserPreference userPreference) {
        User user = getUserByIdOrThrow(userId);

        user.setUserPreference(userPreference);
        user.setHasCompletedOnboarding(true);

        return mongoOperations.save(user);
    }

    public UserPreference getUserPreferences(String userId) {
        User user = getUserByIdOrThrow(userId);
        return user.getUserPreference();
    }

    public User getUserByEmail(String email) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        User user = mongoOperations.findOne(query, User.class);

        if (user == null) {
            throw new UserNotFoundException("Användaren med email: " + email + " hittades inte");
        }
        return user;
    }

    public User getUserByIdOrThrow(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(userId));
        User user = mongoOperations.findOne(query, User.class);

        if (user == null) {
            throw new UserNotFoundException("Användaren med id: " + userId + " hittades inte");
        }
        return user;
    }

    public User saveUser(User user) {
        return mongoOperations.save(user);
    }
}
