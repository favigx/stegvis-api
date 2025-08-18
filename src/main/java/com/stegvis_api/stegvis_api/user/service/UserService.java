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

    public User saveUser(User user) {
        return mongoOperations.save(user);
    }

    public User setUserPreferences(String userId, UserPreference userPreference) {
        User user = getUserById(userId);

        if (user == null) {
            throw new UserNotFoundException("Användare med id " + userId + " hittades inte");
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