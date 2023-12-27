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
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;


@SpringBootTest
@ActiveProfiles("test")
public class BasketTests {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private BasketService basketService;
    private UserService userService;
    private PurchaseService purchaseService;
    private PaintingService paintingService;
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
    public void testDontAddAlreadyAddedPainting(){
        // Create User to be assigned
        User user = new User();
        user.setId("12");
        Basket basket = new Basket();
        basket.setId("1234");
        user.setBasket(basket);
        userService.createUser(user);
        Artist artist = new Artist();
        artist.setId("1");
        Painting painting = new Painting();
        painting.setId("123");
        artistService.addPainting(painting,"1");
        userService.addPaintingToBasket("12","123");
        userService.addPaintingToBasket("12","123"); // Should throw a message that the painting is already in basket.
    }
    @Test
    public void testEmptyBasketWhenPurchaseMade(){ // To test if the basket is empty after purchase
        // Create User to be assigned
        User user = new User();
        user.setId("12");

        Basket basket = new Basket();
        basket.setId("1234");
        user.setBasket(basket);
        userService.createUser(user)
        ;
        // Create paintings
        Artist artist = new Artist();
        artist.setId("1");

        // Create paintings
        Painting painting = new Painting();
        painting.setId("123");
        Painting painting2 = new Painting();
        painting2.setId("124");

        artistService.addPainting(painting,"1");
        artistService.addPainting(painting2,"1");

        userService.addPaintingToBasket("12","123");
        userService.addPaintingToBasket("12","124");
        Purchase purchase = new Purchase();
        purchase.setPaintings(basket.getPaintings());
        purchase.setUserID("12");
        purchaseService.createPurchase(purchase);
        if(basketService.getBasketById("1234").get().getPaintings() == null){
            //WORKS CORRECTLY
        }
    }
    @Test
    public void testIfTotalCalculatedCorrectly(){ // To test if total price is calculated correctly
        // Create User to be assigned
        User user = new User();
        user.setId("12");
        Basket basket = new Basket();
        basket.setId("1234");
        user.setBasket(basket);
        userService.createUser(user);

        Artist artist = new Artist();
        artist.setId("1");
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
        if(basketService.getBasketById("1234").get().getTotal() == 50.){
            //WORKS CORRECTLY
        }
    }
}







