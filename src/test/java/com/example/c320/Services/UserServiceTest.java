package com.example.c320.Services;
import com.example.c320.Entities.User;
import com.example.c320.Repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void getUserById_WhenUserExists() {
        // Arrange
        String userId = "123";
        User expectedUser = new User();
        expectedUser.setId(userId);
        given(userRepository.findById(userId)).willReturn(Optional.of(expectedUser));
        // Act
        Optional<User> userOptional = userService.getUserById(userId);
        // Assert
        assertTrue(userOptional.isPresent(), "User should be present");
        User user = userOptional.get();
        assertEquals(userId, user.getId(), "User ID should match");
    }

    @Test
    public void getUserById_WhenUserDoesNotExist() {
        // Arrange
        String userId = "123";
        given(userRepository.findById(userId)).willReturn(Optional.empty());
        // Act
        Optional<User> result = userService.getUserById(userId);
        // Assert
        assertFalse(result.isPresent(), "User should not be present");
    }




}