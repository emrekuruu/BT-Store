package com.example.c320.Repositories;

import com.example.c320.Entities.Purchase;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PurchaseRepository extends MongoRepository<Purchase,String> {
}
