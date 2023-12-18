import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.data.mongodb.uri=none", // Disable MongoDB connection for tests
})
class C320ApplicationTests {

    @Test
    void contextLoads() {
        // Your test logic here
    }
}
