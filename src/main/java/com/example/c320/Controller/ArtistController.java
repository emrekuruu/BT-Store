package com.example.c320.Controller;
import com.example.c320.Entities.Painting;
import com.example.c320.Services.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.c320.Entities.Artist;
import java.util.List;
import java.util.NoSuchElementException;

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
    @GetMapping("/email/{email}")
    public ResponseEntity<Artist> getArtistByEmail(@PathVariable String email) {
        return artistService.getArtistByEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public Artist createArtist(@RequestBody Artist artist) {
        return artistService.createArtist(artist);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginArtist(@RequestParam String username, @RequestParam String password) {
        String artistId = artistService.isArtistRegistered(username, password);
        if (artistId != null) {
            // Adjust the response to include the artist ID or other relevant information
            return ResponseEntity.ok("Artist ID: " + artistId);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @DeleteMapping("delete/{artistId}")
    public ResponseEntity<?> deleteArtist(@PathVariable String artistId) {
        try {
            artistService.deleteArtist(artistId);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Additional endpoints for update and delete

    @PostMapping("/{artistId}/add")
    public Artist addPainting(@RequestBody Painting painting, @PathVariable String artistId) {
        return artistService.addPainting(painting,artistId);
    }

}

