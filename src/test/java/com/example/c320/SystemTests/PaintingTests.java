package com.example.c320.SystemTests;
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
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class PaintingTests {
    @Autowired
    private MongoTemplate mongoTemplate;
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
    public void testIfPriceEditedCorrectly(){
        Painting painting = new Painting();
        painting.setId("123");
        painting.setPrice(10.);
        painting.setName("First Painting");

        Artist artist = new Artist();
        artist.setId("12");

        artistService.createArtist(artist);
        artistService.addPainting(painting,"12");

        Painting uptadePainting = new Painting();
        uptadePainting.setPrice(20.);
        uptadePainting.setId("124");
        uptadePainting.setName("updated");
        paintingService.updatePainting("123",uptadePainting);

        assertEquals(20.0, paintingService.getPaintingById("123").get().getPrice(), 0.01,"Price edited correctly");
    }

}
