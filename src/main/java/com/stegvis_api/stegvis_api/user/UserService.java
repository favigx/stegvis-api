package com.stegvis_api.stegvis_api.user;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final MongoOperations mongoOperations;
    private final PasswordEncoder passwordEncoder;

    public UserService(MongoOperations mongoOperations, PasswordEncoder passwordEncoder) {
        this.mongoOperations = mongoOperations;
        this.passwordEncoder = passwordEncoder;
    }

    public User addUser(UserRegistrationDTO userRegistrationDTO) {

        User user = new User();

        if (findUserByEmail(userRegistrationDTO.getEmail()) != null) {
            throw new RuntimeException("Användarnamnet är upptaget");
        }

        String encryptedPassword = passwordEncoder.encode(userRegistrationDTO.getPassword());

        user.setPassword(encryptedPassword);
        user.setEmail(userRegistrationDTO.getEmail());

        return mongoOperations.save(user);

    }

    private User findUserByEmail(String email) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        return mongoOperations.findOne(query, User.class);
    }

}
