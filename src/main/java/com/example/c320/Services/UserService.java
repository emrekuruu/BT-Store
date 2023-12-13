package com.example.c320.Services;
import com.example.c320.Entities.Basket;
import com.example.c320.Entities.Painting;
import com.example.c320.Entities.User;
import com.example.c320.Repositories.PaintingRepository;
import com.example.c320.Repositories.UserRepository;
import com.example.c320.Repositories.BasketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private PaintingRepository paintingRepository;


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        userRepository.save(user);
        Basket basket = new Basket();
        basket.setUserID(user.getId());
        user.setBasket(basket);
        basketRepository.save(basket);
        return userRepository.save(user);
    }

    public User addPaintingToBasket(String userId, String paintingId) {
        // Retrieve the user and painting as before
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        Painting painting = paintingRepository.findById(paintingId)
                .orElseThrow(() -> new NoSuchElementException("Painting not found with ID: " + paintingId));

        Basket basket = user.getBasket();
        List<Painting> paintings = basket.getPaintings();

        // Check if the painting is already in the basket by comparing IDs
        boolean paintingExists = paintings.stream()
                .anyMatch(p -> p.getId().equals(painting.getId()));

        if (!paintingExists) {
            paintings.add(painting);
            // Update the total price in the basket
            double newTotal = basket.getTotal() + painting.getPrice();
            basket.setTotal(newTotal);
        }

        // Save the updated basket and user as before
        basketRepository.save(basket);
        return userRepository.save(user);
    }
    public User removePaintingFromBasket(String userId, String paintingId) {
        // Retrieve the user and painting
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        Painting painting = paintingRepository.findById(paintingId)
                .orElseThrow(() -> new NoSuchElementException("Painting not found with ID: " + paintingId));

        Basket basket = user.getBasket();
        List<Painting> paintings = basket.getPaintings();

        // Check if the painting is in the basket
        boolean paintingExists = paintings.stream()
                .anyMatch(p -> p.getId().equals(painting.getId()));

        if (paintingExists) {
            paintings.removeIf(p -> p.getId().equals(painting.getId()));
            // Update the total price in the basket
            double newTotal = basket.getTotal() - painting.getPrice();
            basket.setTotal(newTotal >= 0 ? newTotal : 0); // Ensure total doesn't go negative
        }

        // Save the updated basket and user
        basketRepository.save(basket);
        return userRepository.save(user);
    }


    // Additional methods for update and delete
}
