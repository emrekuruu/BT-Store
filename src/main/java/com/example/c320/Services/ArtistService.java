package com.example.c320.Services;
import com.example.c320.Entities.Artist;
import com.example.c320.Repositories.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ArtistService {

    @Autowired
    private ArtistRepository artistRepository;
    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }
    public Optional<Artist> getArtistById(String id) { return artistRepository.findById(id); }
    public Optional<Artist> getArtistByName(String name) { return artistRepository.findByName(name); }
    public Artist createArtist(Artist artist) {
        return artistRepository.save(artist);
    }

    // Additional methods for update and delete
}
