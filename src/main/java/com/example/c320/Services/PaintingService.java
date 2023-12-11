package com.example.c320.Services;
import com.example.c320.Entities.Painting;
import com.example.c320.Repositories.PaintingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PaintingService {

    @Autowired
    private PaintingRepository paintingRepository;
    public List<Painting> getAllPaintings() {
        return paintingRepository.findAll();
    }
    public Painting getPaintingById(String id) {
        return paintingRepository.findById(id).orElse(null);
    }
    public Painting createPainting(Painting painting) {
        return paintingRepository.save(painting);
    }

    // Additional methods for update and delete
}
