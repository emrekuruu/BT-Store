package com.example.c320.Repositories;
import com.example.c320.Entities.Painting;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaintingRepository extends MongoRepository<Painting, String> {
    // Custom query methods can be defined here
}
