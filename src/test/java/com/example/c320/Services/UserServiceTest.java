package com.example.c320.Services;
import com.example.c320.Entities.Basket;
import com.example.c320.Entities.Painting;
import com.example.c320.Entities.Purchase;
import com.example.c320.Entities.User;
import com.example.c320.Repositories.BasketRepository;
import com.example.c320.Repositories.PaintingRepository;
import com.example.c320.Repositories.PurchaseRepository;
import com.example.c320.Repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
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
    @Mock
    private PurchaseRepository purchaseRepository;
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

        Double total = basket.getTotal();

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
        assertEquals(total + painting.getPrice(), updatedUser.getBasket().getTotal());
    }

    @Test
    public void addPaintingToBasket_WhenPaintingAlreadyInBasket_NoChange() {
        // Arrange
        String userId = "user123";
        String paintingId = "painting123";
        User user = new User();
        Basket basket = new Basket();
        Painting painting = new Painting();
        painting.setId(paintingId);
        painting.setPrice(20.0);
        basket.getPaintings().add(painting);
        basket.setTotal(20.0);
        user.setBasket(basket);
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(paintingRepository.findById(paintingId)).willReturn(Optional.of(painting));
        // Act
        userService.addPaintingToBasket(userId, paintingId);
        // Assert
        assertEquals(1, user.getBasket().getPaintings().size(), "No additional paintings should be added");
        assertEquals(20.0, user.getBasket().getTotal(), "Total should remain unchanged");
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
    @Test
    public void removePaintingFromBasket_Success() {
        // Arrange
        String userId = "user123";
        String paintingId = "painting123";
        User user = new User();
        user.setId(userId);
        Basket basket = new Basket();
        Painting painting = new Painting();
        painting.setId(paintingId);
        painting.setPrice(20.0);
        basket.getPaintings().add(painting);
        basket.setTotal(20.0);
        user.setBasket(basket);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(paintingRepository.findById(paintingId)).willReturn(Optional.of(painting));
        // Mock saving of user and basket if necessary
        given(userRepository.save(any(User.class))).willReturn(user);
        given(basketRepository.save(any(Basket.class))).willReturn(basket);

        // Act
        User updatedUser = userService.removePaintingFromBasket(userId, paintingId);

        // Assert
        assertTrue(updatedUser.getBasket().getPaintings().isEmpty(), "Basket should be empty after removing the painting");
        assertEquals(0.0, updatedUser.getBasket().getTotal(), "Total should be updated correctly");
    }

    @Test
    public void removePaintingFromBasket_PaintingNotInBasket_NoChange() {
        // Arrange
        String userId = "user123";
        String paintingId = "paintingNotInBasket123";
        User user = new User();
        user.setId(userId);
        Basket basket = new Basket();
        user.setBasket(basket);

        Painting paintingNotInBasket = new Painting();
        paintingNotInBasket.setId(paintingId);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(paintingRepository.findById(paintingId)).willReturn(Optional.of(paintingNotInBasket));
        given(userRepository.save(any(User.class))).willReturn(user);

        // Act
        User updatedUser = userService.removePaintingFromBasket(userId, paintingId);

        // Assert
        assertTrue(updatedUser.getBasket().getPaintings().isEmpty(), "Basket should remain unchanged");
        assertEquals(0.0, updatedUser.getBasket().getTotal(), "Total should remain unchanged");
    }

    @Test
    public void deleteUser_Success() {
        // Arrange
        String userId = "user123";
        User user = new User();
        user.setId(userId);
        Basket basket = new Basket();
        user.setBasket(basket);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        willDoNothing().given(userRepository).delete(user);
        willDoNothing().given(basketRepository).delete(basket);

        // Act
        userService.deleteUser(userId);

        // Assert
        verify(userRepository, times(1)).delete(user);
        verify(basketRepository, times(1)).delete(basket);
    }


    @Test
    public void deleteUser_UserNotFound() {
        // Arrange
        String userId = "nonexistentUser";
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> {
            userService.deleteUser(userId);
        });

        verify(userRepository, never()).delete(any(User.class));
        verify(basketRepository, never()).delete(any(Basket.class));
    }

    @Test
    public void purchase_Success() {
        // Arrange
        String userId = "user123";
        User user = new User();
        user.setId(userId);
        Basket basket = new Basket();
        Painting painting = new Painting();
        painting.setPrice(100.0);
        basket.getPaintings().add(painting);
        basket.setTotal(100.0);
        user.setBasket(basket);

        List<Painting> paintings = basket.getPaintings();

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(basketRepository.save(any(Basket.class))).willReturn(basket);
        given(userRepository.save(any(User.class))).willReturn(user);

        // Act
        Purchase purchase = userService.purchase(userId);

        // Assert
        assertNotNull(purchase, "Purchase should not be null");
        assertEquals(paintings,purchase.getPaintings(), "Purhase should include the bought items");
        assertEquals(100.0, purchase.getTotal(), "Total should be reset to 0 in the purchase");
        assertTrue(user.getBasket().getPaintings().isEmpty(), "Basket should be empty after purchase");
        assertEquals(0.0, user.getBasket().getTotal(), "Basket total should be reset to 0 after purchase");
    }

    @Test
    public void purchase_WhenBasketEmpty_ThrowsException() {
        // Arrange
        String userId = "user123";
        User user = new User();
        user.setId(userId);
        Basket basket = new Basket();
        user.setBasket(basket);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            userService.purchase(userId);
        }, "Should throw IllegalStateException when basket is empty");
    }


}