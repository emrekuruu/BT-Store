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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class ArtistTests {

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

    @Test
    public void testFilterbyNameExistingArtist() {
        Artist artist = new Artist();
        artist.setId("123");
        artist.setName("Picasso");
        artistService.createArtist(artist);
        Artist found = artistService.getArtistByName("Picasso").orElse(null);
        assertEquals(artist.getId(), found.getId(),"Filtering by name works properly");

    }

    @Test
    public void testFilterbyIdExistingArtist() {
        Artist artist = new Artist();
        artist.setId("123");
        artist.setName("Picasso");
        artistService.createArtist(artist);
        Artist found = artistService.getArtistById("123").orElse(null);
        assertEquals(artist.getId(), found.getId(),"Filtering by id works properly");
    }

    @Test
    public void testIfDeletedArtistsPaintingsDeleted() {
        //Create Artists
        Artist artist = new Artist();
        artist.setId("1");
        artistService.createArtist(artist);
        // Create paintings
        Painting painting = new Painting();
        painting.setId("123");
        Painting painting2 = new Painting();
        painting2.setId("124");
        artistService.addPainting(painting, "1");
        artistService.addPainting(painting2, "1");
        artistService.deleteArtist("1");
        Painting found = paintingService.getPaintingById("123").orElse(null);
        Painting found2 = paintingService.getPaintingById("124").orElse(null);
        assertNull(found,"Painting no longer exist");
        assertNull(found2,"Painting2 no longer exist");
    }
    @Test
    public void testIfDeletedArtistsPaintingsDeletedFromUsersBasket() {
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

        artistService.addPainting(painting, "1");
        artistService.addPainting(painting2, "1");

        userService.addPaintingToBasket("12", "123");
        userService.addPaintingToBasket("12", "124");
        artistService.deleteArtist("1");

        List<Painting> found = userService.getUserById("12").get().getBasket().getPaintings();
        assertTrue(found.isEmpty(),"Deleted artists paintings deleted from user's basket");
    }
    @Test
    public void testIfExistingUserLoginWithValidPass(){
        // Arrange
        String username = "testArtist";
        String password = "testPassword";
        Artist expectedArtist = new Artist();
        expectedArtist.setUsername(username);
        expectedArtist.setPassword(password);
        artistService.createArtist(expectedArtist);

        // Act
        Artist authenticatedArtist = artistService.isArtistRegistered(username, password);

        // Assert
        assertNotNull(authenticatedArtist, "Artist should be authenticated");
        assertEquals(username, authenticatedArtist.getUsername(), "Username should match");
        assertEquals(password, authenticatedArtist.getPassword(), "Password should match");
    }

    @Test
    public void TestIfExistingUserLoginWithInvalidPass(){
        // Arrange
        String username = "testArtist";
        String correctPassword = "correctPassword";
        String incorrectPassword = "incorrectPassword";
        Artist expectedArtist = new Artist();
        expectedArtist.setUsername(username);
        expectedArtist.setPassword(correctPassword);
        artistService.createArtist(expectedArtist);
        // Act
        Artist authenticatedArtist = artistService.isArtistRegistered(username, incorrectPassword);

        // Assert
        assertNull(authenticatedArtist, "Authentication should fail for incorrect password");

    }
    @Test
    public void TestIfNonExistingUserLogin(){
        // Arrange
        String username = "nonexistentArtist";
        String password = "testPassword";
        Artist artist = new Artist();
        artist.setUsername(username);
        artist.setPassword(password);
        // Act
        Artist authenticatedArtist = artistService.isArtistRegistered(username, password);

        // Assert
        assertNull(authenticatedArtist, "Authentication should fail for invalid username");

    }
}
