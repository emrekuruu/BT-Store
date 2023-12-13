package com.example.c320.Services;
import com.example.c320.Entities.Basket;
import com.example.c320.Entities.Painting;
import com.example.c320.Entities.User;
import com.example.c320.Repositories.BasketRepository;
import com.example.c320.Repositories.PaintingRepository;
import com.example.c320.Repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PaintingRepository paintingRepository;
    @Mock
    private BasketRepository basketRepository;
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


    @Test
    public void addPaintingToBasket_Success() {
        // Arrange
        String userId = "user123";
        String paintingId = "painting123";
        User user = new User();
        user.setId(userId);
        Basket basket = new Basket();
        user.setBasket(basket);

        Painting painting = new Painting();
        painting.setId(paintingId);
        painting.setPrice(20.2);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(paintingRepository.findById(paintingId)).willReturn(Optional.of(painting));
        // Mock saving of user and basket if necessary
        given(userRepository.save(any(User.class))).willReturn(user);
        given(basketRepository.save(any(Basket.class))).willReturn(basket);

        // Act
        User updatedUser = userService.addPaintingToBasket(userId, paintingId);

        // Assert
        assertNotNull(updatedUser.getBasket());
        assertTrue(updatedUser.getBasket().getPaintings().contains(painting));
    }

    @Test
    public void addPaintingToBasket_UserNotFound() {
        // Arrange
        String userId = "nonexistentUser";
        String paintingId = "painting123";

        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> {
            userService.addPaintingToBasket(userId, paintingId);
        });
    }

    @Test
    public void addPaintingToBasket_PaintingNotFound() {
        // Arrange
        String userId = "user123";
        String paintingId = "nonexistentPainting";
        User user = new User();
        user.setId(userId);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(paintingRepository.findById(paintingId)).willReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> {
            userService.addPaintingToBasket(userId, paintingId);
        });
    }

}