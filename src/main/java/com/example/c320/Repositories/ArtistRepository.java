package com.example.c320.Repositories;
import com.example.c320.Entities.Artist;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArtistRepository extends MongoRepository<Artist, String> {
    // Custom query methods can be defined here
}
