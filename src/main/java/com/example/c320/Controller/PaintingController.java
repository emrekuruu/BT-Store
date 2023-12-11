package com.example.c320.Controller;
import com.example.c320.Services.PaintingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.c320.Entities.Painting;
import java.util.List;

@RestController
@RequestMapping("/painting")
public class PaintingController {

    @Autowired
    private PaintingService paintingService;

    @GetMapping
    public List<Painting> getAllPaintings() {
        return paintingService.getAllPaintings();
    }

    @GetMapping("/{id}")
    public Painting getPaintingById(@PathVariable String id) {
        return paintingService.getPaintingById(id);
    }

    @PostMapping
    public Painting createPainting(@RequestBody Painting painting) {
        return paintingService.createPainting(painting);
    }

    // Additional endpoints for update and delete
}
