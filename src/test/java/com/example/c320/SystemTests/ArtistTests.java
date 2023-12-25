package com.example.c320.SystemTests;

import com.example.c320.Entities.Purchase;
import com.example.c320.Services.BasketService;
import com.example.c320.Entities.User;
import com.example.c320.Services.PurchaseService;
import com.example.c320.Services.UserService;
import com.example.c320.Entities.Painting;
import com.example.c320.Services.PaintingService;
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
    private BasketService basketService;
    private UserService userService;
    private PurchaseService purchaseService;
    private ArtistService artistService;
    private PaintingService paintingService;
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

        // Save the artist using the repository
        artistService.createArtist(artist);

        // Retrieve and verify the artist
        Artist found = artistService.getArtistById("123").orElse(null);
        assertNotNull(found, "Artist should be found in the database");
    }

    @Test
    public void testAddAndRetrieveNonExistingArtist() {
        // Retrieve and verify the artist
        Artist artist = new Artist();
        artist.setId("123");

        // Dont add it to the database

        Artist found = artistService.getArtistById("123").orElse(null);
        assertNull(found, "Artist should be found in the database");
    }

    @Test
    public void testFilterbyNameExistingArtist(){
        Artist artist = new Artist();
        artist.setId("123");
        artist.setName("Picasso");
        artistService.createArtist(artist);
        Artist found = artistService.getArtistByName("Picasso").orElse(null);
        //Check if found == artist
    }

    @Test
    public void testFilterbyIdExistingArtist(){
        Artist artist = new Artist();
        artist.setId("123");
        artist.setName("Picasso");
        artistService.createArtist(artist);
        Artist found = artistService.getArtistById("123").orElse(null);
        //Check if found == artist
    }

    public void testIfDeletedArtistsPaintingsDeleted(){
        //Create Artists
        Artist artist = new Artist();
        artist.setId("1");
        // Create paintings
        Painting painting = new Painting();
        painting.setId("123");
        Painting painting2 = new Painting();
        painting2.setId("124");
        artistService.addPainting(painting,"1");
        artistService.addPainting(painting2,"1");
        artistService.deleteArtist("1");
        Painting found = paintingService.getPaintingById("123").orElse(null);
        Painting found2 = paintingService.getPaintingById("124").orElse(null);
    }
}
