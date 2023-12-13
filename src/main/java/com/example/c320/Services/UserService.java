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
        Basket basket = new Basket();
        basket.setUser(user);
        basketRepository.save(basket);
        return userRepository.save(user);
    }

    public User addPaintingToBasket(String userId, String paintingId) {
        // Retrieve the user by ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));

        // Retrieve the painting by ID
        Painting painting = paintingRepository.findById(paintingId)
                .orElseThrow(() -> new NoSuchElementException("Painting not found with ID: " + paintingId));

        // Retrieve or create the basket
        Basket basket = user.getBasket();
        if (basket == null) {
            basket = new Basket();
            basket.setUser(user);
            user.setBasket(basket);
        }

        // Add the painting to the basket
        basket.getPaintings().add(painting);

        // Update the total price in the basket
        double newTotal = basket.getTotal() + painting.getPrice();
        basket.setTotal(newTotal);

        // Save the updated basket and user
        basketRepository.save(basket); // Save the basket
        return userRepository.save(user); // Save the user
    }

    // Additional methods for update and delete
}
