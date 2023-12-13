package com.example.c320.Services;
import com.example.c320.Entities.Painting;
import com.example.c320.Repositories.PaintingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PaintingServiceTest {

    @Mock
    private PaintingRepository paintingRepository;

    @InjectMocks
    private PaintingService paintingService;

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




}