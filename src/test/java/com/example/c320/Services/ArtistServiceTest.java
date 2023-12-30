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
    public void authenticate_ValidCredentials_ReturnsArtist() {
        // Arrange
        String username = "testUser";
        String password = "testPassword";
        Artist expectedArtist = new Artist();
        expectedArtist.setUsername(username);
        expectedArtist.setPassword(password);
        given(artistRepository.findByUsername(username)).willReturn(Optional.of(expectedArtist));

        // Act
        Artist authenticatedArtist = artistService.isArtistRegistered(username, password);

        // Assert
        assertNotNull(authenticatedArtist, "Artist should be authenticated");
        assertEquals(username, authenticatedArtist.getUsername(), "Username should match");
        assertEquals(password, authenticatedArtist.getPassword(), "Password should match");
    }

    @Test
    public void authenticate_InvalidUsername_ReturnsNull() {
        // Arrange
        String username = "nonexistentUser";
        String password = "testPassword";
        given(artistRepository.findByUsername(username)).willReturn(Optional.empty());

        // Act
        Artist authenticatedArtist = artistService.isArtistRegistered(username, password);

        // Assert
        assertNull(authenticatedArtist, "Artist should not be authenticated");
    }

    @Test
    public void authenticate_InvalidPassword_ReturnsNull() {
        // Arrange
        String username = "testUser";
        String correctPassword = "correctPassword";
        String incorrectPassword = "incorrectPassword";
        Artist expectedArtist = new Artist();
        expectedArtist.setUsername(username);
        expectedArtist.setPassword(correctPassword);
        given(artistRepository.findByUsername(username)).willReturn(Optional.of(expectedArtist));

        // Act
        Artist authenticatedArtist = artistService.isArtistRegistered(username, incorrectPassword);

        // Assert
        assertNull(authenticatedArtist, "Artist should not be authenticated");
    }
}