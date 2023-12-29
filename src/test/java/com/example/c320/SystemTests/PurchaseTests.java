package com.example.c320.SystemTests;
import com.example.c320.Entities.Basket;
import com.example.c320.Entities.Purchase;
import com.example.c320.Services.BasketService;
import com.example.c320.Entities.User;
import com.example.c320.Services.PurchaseService;
import com.example.c320.Services.UserService;
import com.example.c320.Entities.Painting;
import com.example.c320.Services.PaintingService;
import com.example.c320.Entities.Artist;
import com.example.c320.Services.ArtistService;
import com.example.c320.Entities.*;
import com.example.c320.Services.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;


import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class PurchaseTests {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private BasketService basketService;
    @Autowired
    private UserService userService;
    @Autowired
    private PurchaseService purchaseService;
    @Autowired
    private ArtistService artistService;
    @Autowired
    private PaintingService paintingService;
    private static final DockerImageName MONGO_IMAGE = DockerImageName.parse("mongo:4.4.2");
    private static MongoDBContainer mongoDBContainer;


    @BeforeAll
    static void setup() {
        mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.4.2"))
                .waitingFor(Wait.forListeningPort()) // Wait for the container to be ready
                .withStartupTimeout(Duration.ofMinutes(3));
        mongoDBContainer.start();

        // Use the dynamically assigned port
        String uri = String.format("mongodb://%s:%d/testdb",
                mongoDBContainer.getHost(),
                mongoDBContainer.getFirstMappedPort());

        System.setProperty("spring.data.mongodb.uri", uri);
    }

    @BeforeEach
    void clearCollectionsBeforeTest() {
        for (String collectionName : mongoTemplate.getDb().listCollectionNames()) {
            mongoTemplate.remove(new Query(), collectionName);
        }
    }

    @Test
    public void testIfPurchasedPaintingsRemoved(){// To check if the purchased paintings are unable for other users when someone else purchase them
        // Create User to be assigned
        User user = new User();
        user.setId("12");
        userService.createUser(user);

        //Create Artists
        Artist artist = new Artist();
        artist.setId("1");
        artistService.createArtist(artist);

        // Create paintings
        Painting painting = new Painting();
        painting.setId("123");
        Painting painting2 = new Painting();
        painting2.setId("124");
        artistService.addPainting(painting,"1");
        artistService.addPainting(painting2,"1");

        userService.addPaintingToBasket("12","123");
        userService.addPaintingToBasket("12","124");
        userService.purchase("12");

        assertFalse(paintingService.getPaintingById("123").isPresent()); // Assert that painting with ID "123" is not present
        assertFalse(paintingService.getPaintingById("124").isPresent()); // Assert that painting with ID "124" is not present
    }
}
