package com.example.c320.Repositories;

import com.example.c320.Entities.Basket;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BasketRepository extends MongoRepository<Basket, Long> {
    // Custom query methods can be defined here
}
