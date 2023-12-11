package com.example.c320.Controller;
import com.example.c320.Services.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.c320.Entities.Artist;
import java.util.List;

@RestController
@RequestMapping("/artist")
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    @GetMapping
    public List<Artist> getAllArtists() {
        return artistService.getAllArtists();
    }

    @GetMapping("/id/{id}")
    public Artist getArtistById(@PathVariable String id) { return artistService.getArtistById(id); }
    @GetMapping("/name/{name}")
    public Artist getArtistByName(@PathVariable String name) { return artistService.getArtistByName(name); }

    @PostMapping
    public Artist createArtist(@RequestBody Artist artist) {
        return artistService.createArtist(artist);
    }

    // Additional endpoints for update and delete
}
