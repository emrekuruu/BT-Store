package com.example.c320.Services;
import com.example.c320.Entities.Artist;
import com.example.c320.Entities.Painting;
import com.example.c320.Entities.Artist;
import com.example.c320.Entities.Artist;
import com.example.c320.Repositories.ArtistRepository;
import com.example.c320.Repositories.PaintingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ArtistServiceTest {

    @Mock
    private ArtistRepository artistRepository;
    @Mock
    private PaintingRepository paintingRepository;
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
        Optional<Artist> result  = artistService.getArtistById(artistId);
        // Assert
        assertFalse(result.isPresent(), "Artist should not be present");
    }

    @Test
    public void deleteArtist_ArtistNotFound() {
        // Arrange
        String artistId = "nonexistentArtist";
        given(artistRepository.findById(artistId)).willReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> {
            artistService.deleteArtist(artistId);
        });

        verify(artistRepository, never()).delete(any(Artist.class));
    }
   
    @Test
    public void updateArtist() {
    // Arrange
    String artistId = "existingArtist";
    Artist originalArtist = new Artist();
    originalArtist.setId(artistId);
    originalArtist.setName("Original Name");
    originalArtist.setSurname("Original Surname");
    originalArtist.setUsername("original_username");
    originalArtist.setPassword("original_password");

    Artist updatedArtistData = new Artist();
    updatedArtistData.setName("Updated Name");
    updatedArtistData.setSurname("Updated Surname");
    updatedArtistData.setUsername("updated_username");
    updatedArtistData.setPassword("updated_password");

    given(artistRepository.findById(artistId)).willReturn(Optional.of(originalArtist));
    given(artistRepository.save(any(Artist.class))).willAnswer(invocation -> invocation.getArgument(0));

    // Act
    Artist updatedArtist = artistService.updateArtist(artistId, updatedArtistData);

    // Assert
    assertNotNull(updatedArtist);
    assertEquals(updatedArtistData.getName(), updatedArtist.getName());
    assertEquals(updatedArtistData.getSurname(), updatedArtist.getSurname());
    assertEquals(updatedArtistData.getUsername(), updatedArtist.getUsername());
    assertEquals(updatedArtistData.getPassword(), updatedArtist.getPassword());
}
}