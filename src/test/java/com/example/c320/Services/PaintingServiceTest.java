package com.example.c320.Services;
import com.example.c320.Entities.Artist;
import com.example.c320.Entities.Basket;
import com.example.c320.Entities.Painting;
import com.example.c320.Entities.User;
import com.example.c320.Repositories.ArtistRepository;
import com.example.c320.Repositories.BasketRepository;
import com.example.c320.Repositories.PaintingRepository;
import com.example.c320.Repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class PaintingServiceTest {

    @Mock
    private PaintingRepository paintingRepository;

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private BasketRepository basketRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PaintingService paintingService;


    @Captor
    private ArgumentCaptor<Basket> basketCaptor;

    @Captor
    private ArgumentCaptor<Artist> artistCaptor;

    @Captor
    private ArgumentCaptor<User> userCaptor;


    @Test
    public void getPaintingById_WhenPaintingExists() {
        // Arrange
        String paintingId = "123";
        Painting expectedPainting = new Painting();
        expectedPainting.setId(paintingId);
        given(paintingRepository.findById(paintingId)).willReturn(Optional.of(expectedPainting));
        // Act
        Optional<Painting> paintingOptional = paintingService.getPaintingById(paintingId);
        // Assert
        assertTrue(paintingOptional.isPresent(), "Painting should be present");
        Painting painting = paintingOptional.get();
        assertEquals(paintingId, painting.getId(), "Painting ID should match");
    }

    @Test
    public void getPaintingById_WhenPaintingDoesNotExist() {
        // Arrange
        String paintingId = "123";
        given(paintingRepository.findById(paintingId)).willReturn(Optional.empty());
        // Act
        Optional<Painting> result = paintingService.getPaintingById(paintingId);
        // Assert
        assertFalse(result.isPresent(), "Painting should not be present");
    }


    @Test
    public void updatePainting() {
        // Arrange
        String paintingId = "existingPainting";
        Painting originalPainting = new Painting();
        originalPainting.setId(paintingId);
        originalPainting.setName("Original Name");
        originalPainting.setDescription("Original Description");

        Painting updatedData = new Painting();
        updatedData.setName("Updated Name");
        updatedData.setDescription("Updated Description");

        Artist artist = new Artist();
        List<Painting> artistPaintings = new ArrayList<>();
        artistPaintings.add(originalPainting);
        artist.setPaintings(artistPaintings);

        Basket basket = new Basket();
        List<Painting> basketPaintings = new ArrayList<>();
        basketPaintings.add(originalPainting);
        basket.setPaintings(basketPaintings);
        double total = basket.getTotal();

        User user = new User();
        user.setBasket(basket);

        given(paintingRepository.findById(paintingId)).willReturn(Optional.of(originalPainting));
        given(paintingRepository.save(any(Painting.class))).willAnswer(invocation -> invocation.getArgument(0));
        given(artistRepository.findByPaintingsId(paintingId)).willReturn(artist);
        given(basketRepository.findAllByPaintingsId(paintingId)).willReturn(Collections.singletonList(basket));
        given(userRepository.findById(basket.getUserID())).willReturn(Optional.of(user));
        // Act
        Painting updatedPainting = paintingService.updatePainting(paintingId, updatedData);

        // Assert
        assertNotNull(updatedPainting);
        assertEquals(updatedData.getName(), updatedPainting.getName());
        assertEquals(updatedData.getDescription(), updatedPainting.getDescription());

        // Verify that the painting is updated in artist, basket, and user
        verify(artistRepository, times(1)).save(artistCaptor.capture());
        Painting updatedArtistPainting = artistCaptor.getValue().getPaintings().get(0);
        assertEquals(updatedData.getName(), updatedArtistPainting.getName());
        assertEquals(updatedData.getDescription(), updatedArtistPainting.getDescription());

        verify(basketRepository, times(1)).save(basketCaptor.capture());
        Painting updatedBasketPainting = basketCaptor.getValue().getPaintings().get(0);
        assertEquals(updatedData.getName(), updatedBasketPainting.getName());
        assertEquals(updatedData.getDescription(), updatedBasketPainting.getDescription());

        verify(userRepository, times(1)).save(userCaptor.capture());
        User updatedUser = userCaptor.getValue();
        Painting updatedUserBasketPainting = updatedUser.getBasket().getPaintings().get(0);
        assertEquals(updatedData.getName(), updatedUserBasketPainting.getName());
        assertEquals(updatedData.getDescription(), updatedUserBasketPainting.getDescription());
        assertEquals(total-originalPainting.getPrice(),basket.getTotal());
    }

    @Test
    public void deletePainting_Success() {
        // Arrange
        String paintingId = "existingPainting";
        Painting painting = new Painting();
        painting.setId(paintingId);

        Artist artist = new Artist();
        artist.getPaintings().add(painting);

        Basket basket = new Basket();
        basket.getPaintings().add(painting);
        double total = basket.getTotal();

        User user = new User();
        user.setBasket(basket);

        given(paintingRepository.findById(paintingId)).willReturn(Optional.of(painting));
        willDoNothing().given(paintingRepository).delete(painting);
        given(artistRepository.findByPaintingsId(paintingId)).willReturn(artist);
        given(basketRepository.findAllByPaintingsId(paintingId)).willReturn(Collections.singletonList(basket));
        given(userRepository.findById(basket.getUserID())).willReturn(Optional.of(user));

        // Act
        paintingService.deletePainting(paintingId);

        // Assert
        verify(paintingRepository, times(1)).delete(painting);
        verify(artistRepository, times(1)).save(artist);
        verify(basketRepository, times(1)).save(basket);
        verify(userRepository, times(1)).save(user);
        assertEquals(total-painting.getPrice(),basket.getTotal());
    }




}