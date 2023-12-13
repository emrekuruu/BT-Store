package com.example.c320.Services;
import com.example.c320.Entities.Artist;
import com.example.c320.Repositories.ArtistRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ArtistServiceTest {

    @Mock
    private ArtistRepository artistRepository;

    @InjectMocks
    private ArtistService artistService;

    @Test
    public void getArtistById_WhenArtistExists() {
        // Arrange
        String artistId = "123";
        Artist expectedArtist = new Artist();
        expectedArtist.setId(artistId);
        given(artistRepository.findById(artistId)).willReturn(Optional.of(expectedArtist));
        // Act
        Optional<Artist> artistOptional = artistService.getArtistById(artistId);
        // Assert
        assertTrue(artistOptional.isPresent(), "Artist should be present");
        Artist artist = artistOptional.get();
        assertEquals(artistId, artist.getId(), "Artist ID should match");
    }

    @Test
    public void getArtistById_WhenArtistDoesNotExist() {
        // Arrange
        String artistId = "123";
        given(artistRepository.findById(artistId)).willReturn(Optional.empty());
        // Act
        Optional<Artist> result = artistService.getArtistById(artistId);
        // Assert
        assertFalse(result.isPresent(), "Artist should not be present");
    }







}