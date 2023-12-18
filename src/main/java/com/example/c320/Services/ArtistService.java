package com.example.c320.Services;
import com.example.c320.Entities.Artist;
import com.example.c320.Entities.Painting;
import com.example.c320.Entities.Artist;
import com.example.c320.Repositories.ArtistRepository;
import com.example.c320.Repositories.PaintingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ArtistService {

    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private PaintingRepository paintingRepository;
    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }
    public Optional<Artist> getArtistById(String id) { return artistRepository.findById(id); }
    public Optional<Artist> getArtistByName(String name) { return artistRepository.findByName(name); }
    public Artist createArtist(Artist artist) {
        return artistRepository.save(artist);
    }
    public Artist addPainting(Painting painting, String artistId){

        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new NoSuchElementException("Artist not found with ID: " + artistId));

        artist.getPaintings().add(painting);
        painting.setArtistID(artist.getId());
        paintingRepository.save(painting);
        return artistRepository.save(artist);
    }

    public void deleteArtist(String artistId) {
        // Find the artist by ID
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new NoSuchElementException("Artist not found with ID: " + artistId));

        // Delete all paintings by this artist
        List<Painting> paintings = artist.getPaintings();
        paintingRepository.deleteAll(paintings);

        // Delete the artist
        artistRepository.delete(artist);
    }

    public boolean isArtistRegistered(String username, String password) {
        Optional<Artist> artistOpt = artistRepository.findByUsername(username);
        if (artistOpt.isPresent()) {
            Artist artist = artistOpt.get();
            // Compare the plain text passwords directly
            return artist.getPassword().equals(password);
        }
        return false;
    }

    // Additional methods for update and delete
}
