package com.example.c320.Controller;
import com.example.c320.Entities.Painting;
import com.example.c320.Services.PaintingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.c320.Entities.Painting;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/painting")
public class PaintingController {

    @Autowired
    private PaintingService paintingService;
    @GetMapping
    public List<Painting> getAllPaintings() {
        return paintingService.getAllPaintings();
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<Painting> getPaintingById(@PathVariable String id) {
        return paintingService.getPaintingById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PutMapping("/update/{paintingId}")
    public ResponseEntity<Painting> updatePainting(@PathVariable String paintingId, @RequestBody Painting paintingData) {
        try {
            Painting updatedPainting = paintingService.updatePainting(paintingId, paintingData);
            return ResponseEntity.ok(updatedPainting);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    // Additional endpoints for update and delete
}
