package com.example.c320.Services;
import com.example.c320.Entities.Painting;
import com.example.c320.Repositories.PaintingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PaintingService {

    @Autowired
    private PaintingRepository paintingRepository;
    public List<Painting> getAllPaintings() {
        return paintingRepository.findAll();
    }
    public Optional<Painting> getPaintingById(String id) {
        return paintingRepository.findById(id);
    }
    public Painting createPainting(Painting painting) {
        return paintingRepository.save(painting);
    }

    // Additional methods for update and delete
}
