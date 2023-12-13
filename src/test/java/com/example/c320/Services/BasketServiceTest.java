package com.example.c320.Services;
import com.example.c320.Entities.Basket;
import com.example.c320.Repositories.BasketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BasketServiceTest {

    @Mock
    private BasketRepository basketRepository;

    @InjectMocks
    private BasketService basketService;

    @Test
    public void getBasketById_WhenBasketExists() {
        // Arrange
        String basketId = "123";
        Basket expectedBasket = new Basket();
        expectedBasket.setId(basketId);
        given(basketRepository.findById(basketId)).willReturn(Optional.of(expectedBasket));
        // Act
        Optional<Basket> basketOptional = basketService.getBasketById(basketId);
        // Assert
        assertTrue(basketOptional.isPresent(), "Basket should be present");
        Basket basket = basketOptional.get();
        assertEquals(basketId, basket.getId(), "Basket ID should match");
    }

    @Test
    public void getBasketById_WhenBasketDoesNotExist() {
        // Arrange
        String basketId = "123";
        given(basketRepository.findById(basketId)).willReturn(Optional.empty());
        // Act
        Optional<Basket> result = basketService.getBasketById(basketId);
        // Assert
        assertFalse(result.isPresent(), "Basket should not be present");
    }




}