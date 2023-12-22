package com.example.c320.SystemTests;

import com.example.c320.Entities.Basket;
import com.example.c320.Services.BasketService;
import com.example.c320.Entities.User;
import com.example.c320.Services.UserService;
import com.example.c320.Entities.Painting;
import com.example.c320.Services.PaintingService;
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
public class BasketTests {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private BasketService basketService;
    private UserService userService;
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
    public void testAssignExistingUserId(){
        // Create User to be assigned
        User user = new User();
        user.setId("123");

        // Create basket to assign
        Basket basket = new Basket();
        basket.setId("1234");
        basket.setUserID("123");

        basketService.createBasket(basket);

        Basket found = basketService.getBasketById("1234").orElse(null);
        assertNotNull(found, "Basket should be found in the database");
    }

    @Test
    public void testAssignNonExistingUserId(){
        // No user created to be assigned

        // Create basket to assign
        Basket basket = new Basket();
        basket.setId("1234");
        basket.setUserID("12");

        Basket found = basketService.getBasketById("1234").orElse(null);
        assertNull(found, "Basket should not be found in the database");
    }

    @Test
    public void testDontAddAlreadyAddedPainting(){
        // Create User to be assigned
        User user = new User();
        user.setId("12");
        // Create User to be assigned
        Painting painting = new Painting();
        painting.setId("123");
        Basket basket = new Basket();
        basket.setId("1234");
        user.setBasket(basket);
        userService.addPaintingToBasket("12","123");
        userService.addPaintingToBasket("12","123"); // Should throw a message that the painting is already in basket.
    }
    @Test
    public void testAddPaintingIntoBasketandRetrievePaintings(){
        // Create User to be assigned
        User user = new User();
        user.setId("12");
        // Create User to be assigned
        Painting painting = new Painting();
        painting.setId("123");
        Basket basket = new Basket();
        basket.setId("1234");
        user.setBasket(basket);
        userService.addPaintingToBasket("12","123");

        Basket found = basketService.getBasketById("1234").orElse(null);
        assertNotNull(found, "Basket should be found in the database");
    }

}







