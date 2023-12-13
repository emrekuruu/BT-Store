package com.example.c320.Repositories;
import com.example.c320.Entities.Artist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface ArtistRepository extends MongoRepository<Artist, String> {

    // In the @Query annotation, ?0 refers to the first parameter of the method. You can use ?1, ?2, etc., to refer to subsequent method parameters if needed.
    @Query("{ 'name' : ?0 }")
    Optional<Artist> findByName(String name);

}
