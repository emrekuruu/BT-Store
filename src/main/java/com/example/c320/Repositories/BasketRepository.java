package com.example.c320.Repositories;

import com.example.c320.Entities.Basket;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BasketRepository extends MongoRepository<Basket, String> {
    List<Basket> findAllByPaintingsId(String paintingId);
    // Custom query methods can be defined here
}
