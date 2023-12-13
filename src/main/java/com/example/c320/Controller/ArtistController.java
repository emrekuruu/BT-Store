package com.example.c320.Controller;
import com.example.c320.Entities.Painting;
import com.example.c320.Services.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Artist> getArtistById(@PathVariable String id) {
        return artistService.getArtistById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/name/{name}")
    public ResponseEntity<Artist> getArtistByName(@PathVariable String name) {
        return artistService.getArtistByName(name)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping
    public Artist createArtist(@RequestBody Artist artist) {
        return artistService.createArtist(artist);
    }
    // Additional endpoints for update and delete

    @PostMapping("/{artistId}/add")
    public Artist addPainting(@RequestBody Painting painting, @PathVariable String artistId) {
        return artistService.addPainting(painting,artistId);
    }

}

