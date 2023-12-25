package com.example.c320.SystemTests;
import com.example.c320.Entities.*;
import com.example.c320.Services.*;
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

@SpringBootTest
@ActiveProfiles("test")
public class PurchaseTests {
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
    public void testIfPurchasedPaintingsRemoved(){// To check if the purchased paintings are unable for other users when someone else purchase them
        // Create User to be assigned
        User user = new User();
        user.setId("12");
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
        //Create basket
        Basket basket = new Basket();
        basket.setId("1234");
        user.setBasket(basket);
        userService.createUser(user);
        userService.addPaintingToBasket("12","123");
        userService.addPaintingToBasket("12","124");
        Purchase purchase = new Purchase();
        purchase.setPaintings(basket.getPaintings());
        purchase.setUserID("12");
        purchaseService.createPurchase(purchase);
        Painting found1 =  paintingService.getPaintingById("123").get();
        Painting found2 =  paintingService.getPaintingById("124").get();
        //CHECK IF THEY ARE EXIST
    }
}
