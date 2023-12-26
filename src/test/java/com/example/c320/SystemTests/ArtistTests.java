package com.example.c320.SystemTests;

import com.example.c320.Entities.Artist;
import com.example.c320.Services.ArtistService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class ArtistTests {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ArtistService artistService;
    private static final DockerImageName MONGO_IMAGE = DockerImageName.parse("mongo:4.4.2");
    private static MongoDBContainer mongoDBContainer;

    @BeforeAll
    static void setup() {
        mongoDBContainer = new MongoDBContainer(MONGO_IMAGE);
        mongoDBContainer.start();
        System.setProperty("spring.data.mongodb.uri", mongoDBContainer.getReplicaSetUrl());
    }

    @BeforeEach
    void clearDatabaseBeforeTest() {
        mongoTemplate.getDb().drop(); // This will drop the entire database
    }

    @AfterAll
    static void tearDown() {
        if (mongoDBContainer != null) {
            mongoDBContainer.stop();
        }
    }

    @Test
    public void testAddAndRetrieveArtist() {
        
        Artist artist = new Artist();
        artist.setId("123");

        // Save the artist
        artistService.createArtist(artist);

        // Retrieve and verify the artist
        Artist found = artistService.getArtistById("123").orElse(null);
        assertNotNull(found, "Artist should be found in the database");
    }

    @Test
    public void testAddAndRetrieveNonExistingArtist() {

        Artist artist = new Artist();
        artist.setId("123");

        // Dont add it to the database

        Artist found = artistService.getArtistById("123").orElse(null);
        assertNull(found, "Artist should not be found in the database");
    }
}
