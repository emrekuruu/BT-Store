package com.example.c320.Services;
import com.example.c320.Entities.Painting;
import com.example.c320.Entities.Basket;
import com.example.c320.Entities.Artist;
import com.example.c320.Entities.User;
import com.example.c320.Repositories.ArtistRepository;
import com.example.c320.Repositories.BasketRepository;
import com.example.c320.Repositories.PaintingRepository;
import com.example.c320.Repositories.UserRepository;
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
    @Autowired
    private UserRepository userRepository;

    public List<Painting> getAllPaintings() {
        return paintingRepository.findAll();
    }
    public Optional<Painting> getPaintingById(String id) {
        return paintingRepository.findById(id);
    }
    public Painting updatePainting(String paintingId, Painting updatedPaintingData) {
        Painting painting = paintingRepository.findById(paintingId)
                .orElseThrow(() -> new NoSuchElementException("Painting not found with ID: " + paintingId));

        // Update the painting's properties
        painting.setName(updatedPaintingData.getName());
        painting.setDescription(updatedPaintingData.getDescription());
        painting.setPrice(updatedPaintingData.getPrice());
        painting.setUrl(updatedPaintingData.getUrl());
        // Update other fields as needed

        // Save the updated painting
        paintingRepository.save(painting);

        // Update painting in artist's collection
        Artist artist = artistRepository.findByPaintingsId(paintingId);
        artist.getPaintings().removeIf(p -> p.getId().equals(paintingId));
        artist.getPaintings().add(painting);
        artistRepository.save(artist);

        // Update painting in all baskets
        List<Basket> baskets = basketRepository.findAllByPaintingsId(paintingId);
        for (Basket basket : baskets) {
            List<Painting> paintings = basket.getPaintings();
            paintings.removeIf(p -> p.getId().equals(paintingId));
            paintings.add(painting);
            basket.setPaintings(paintings);
            basketRepository.save(basket);

            //We also need to update the users that is the owner of this basket
            User user = userRepository.findById(basket.getUserID()).get();
            user.setBasket(basket);
            userRepository.save(user);
        }



        return painting;
    }
    public void deletePainting(String paintingId) {
        // Find the painting
        Painting painting = paintingRepository.findById(paintingId)
                .orElseThrow(() -> new NoSuchElementException("Painting not found with ID: " + paintingId));

        // Remove painting from artist's collection
        Artist artist = artistRepository.findByPaintingsId(paintingId);
        artist.getPaintings().removeIf(p -> p.getId().equals(paintingId));
        artistRepository.save(artist);

        // Remove painting from all baskets and update users
        List<Basket> baskets = basketRepository.findAllByPaintingsId(paintingId);
        for (Basket basket : baskets) {
            List<Painting> paintings = basket.getPaintings();
            paintings.removeIf(p -> p.getId().equals(paintingId));
            basket.setPaintings(paintings);
            basketRepository.save(basket);

            //We also need to update the users that is the owner of this basket
            User user = userRepository.findById(basket.getUserID()).get();
            user.setBasket(basket);
            userRepository.save(user);
        }
        // Delete the painting
        paintingRepository.delete(painting);
    }



    // Additional methods for update and delete
}
