package com.example.c320.Repositories;

import com.example.c320.Entities.Artist;
import com.example.c320.Entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    @Query("{ 'email' : ?0 }")
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
}
