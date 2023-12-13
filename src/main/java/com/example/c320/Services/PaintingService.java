package com.example.c320.Services;
import com.example.c320.Entities.Painting;
import com.example.c320.Entities.Basket;
import com.example.c320.Entities.Artist;
import com.example.c320.Repositories.ArtistRepository;
import com.example.c320.Repositories.BasketRepository;
import com.example.c320.Repositories.PaintingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PaintingService {

    @Autowired
    private PaintingRepository paintingRepository;
    @Autowired
    private BasketRepository basketRepository;
    @Autowired
    private ArtistRepository artistRepository;
    public List<Painting> getAllPaintings() {
        return paintingRepository.findAll();
    }
    public Optional<Painting> getPaintingById(String id) {
        return paintingRepository.findById(id);
    }
    public Painting createPainting(Painting painting) {
        return paintingRepository.save(painting);
    }
    public Painting updatePainting(String paintingId, Painting updatedPaintingData) {

        Painting painting = paintingRepository.findById(paintingId)
                .orElseThrow(() -> new NoSuchElementException("Painting not found with ID: " + paintingId));

        // Update the painting's properties
        painting.setName(updatedPaintingData.getName());
        painting.setDescription(updatedPaintingData.getDescription());
        painting.setPrice(updatedPaintingData.getPrice());
        // Add any other fields that you need to update

        // Update all the objects containing this painting ( 'i.e' the baskets containing this painting and the owner of this painting )
        Artist artist = artistRepository.findByPaintingsId(paintingId);
        artist.getPaintings().replaceAll(p -> p.getId().equals(paintingId) ? painting : p);
        artistRepository.save(artist);

        List<Basket> baskets = basketRepository.findAllByPaintingsId(paintingId);
        for (Basket basket : baskets) {
            List<Painting> paintings = basket.getPaintings();
            paintings.replaceAll(p -> p.getId().equals(paintingId) ? painting : p);
            basketRepository.save(basket);
        }

        // Save the updated painting
        return paintingRepository.save(painting);
    }

    // Additional methods for update and delete
}
