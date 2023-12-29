package com.example.c320.SystemTests;
import com.example.c320.Entities.*;
import com.example.c320.Services.BasketService;
import com.example.c320.Services.PurchaseService;
import com.example.c320.Services.UserService;
import com.example.c320.Services.PaintingService;
import com.example.c320.Services.ArtistService;
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

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
public class BasketTests {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BasketService basketService;

    @Autowired
    private UserService userService;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private PaintingService paintingService;

    @Autowired
    private ArtistService artistService;
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
    public void testDontAddAlreadyAddedPainting(){
        // Create User to be assigned
        User user = new User();
        user.setId("12");
        userService.createUser(user);

        Artist artist = new Artist();
        artist.setId("1");
        artistService.createArtist(artist);

        Painting painting = new Painting();
        painting.setId("123");

        artistService.addPainting(painting,"1");
        userService.addPaintingToBasket("12","123");
        userService.addPaintingToBasket("12","123");
        assertEquals(userService.getUserById("12").get().getBasket().getPaintings().size(),1,"Painting shouldnt be added twice");
    }
    @Test
    public void testEmptyBasketWhenPurchaseMade(){ // To test if the basket is empty after purchase
        // Create User to be assigned
        User user = new User();
        user.setId("12");
        userService.createUser(user);

        // Create paintings
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

        assertTrue(userService.getUserById("12").get().getBasket().getPaintings().isEmpty(),"Basket is not empty");

    }
    @Test
    public void testIfTotalCalculatedCorrectly(){ // To test if total price is calculated correctly
        // Create User to be assigned
        User user = new User();
        user.setId("12");
        userService.createUser(user);

        Artist artist = new Artist();
        artist.setId("1");
        artistService.createArtist(artist);

        Painting painting = new Painting();
        painting.setId("123");

        painting.setPrice(10.);
        Painting painting2 = new Painting();

        painting2.setId("124");
        painting2.setPrice(20.);

        Painting painting3 = new Painting();
        painting3.setId("125");
        painting3.setPrice(40.);

        artistService.addPainting(painting,"1");
        artistService.addPainting(painting2,"1");
        artistService.addPainting(painting3,"1");
        userService.addPaintingToBasket("12","123");
        userService.addPaintingToBasket("12","124");
        userService.addPaintingToBasket("12","125");
        userService.removePaintingFromBasket("12","124");
        assertEquals(50.0, userService.getUserById("12").get().getBasket().getTotal(), 0.01,"Total calculated correctly");
    }
}







