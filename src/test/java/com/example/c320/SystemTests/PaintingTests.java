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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class PaintingTests {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
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
    public void testIfPriceEditedCorrectly(){
        Painting painting = new Painting();
        painting.setId("123");
        painting.setPrice(10.);
        painting.setName("First Painting");
        Artist artist = new Artist();
        artist.setId("12");
        artistService.addPainting(painting,"12");
        Painting uptadePainting = new Painting();
        uptadePainting.setPrice(20.);
        uptadePainting.setId("124");
        uptadePainting.setName("updated");
        paintingService.updatePainting("123",uptadePainting);
        if(paintingService.getPaintingById("123").get().getPrice() == 20.){
            //Works corretly
        }
    }

}
