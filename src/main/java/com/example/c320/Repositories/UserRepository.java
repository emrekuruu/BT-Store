package com.example.c320.Repositories;

import com.example.c320.Entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, Long> {
    // Custom query methods can be defined here
}
