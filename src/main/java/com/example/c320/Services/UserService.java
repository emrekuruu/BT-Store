package com.example.c320.Services;
import com.example.c320.Entities.*;
import com.example.c320.Repositories.PaintingRepository;
import com.example.c320.Repositories.PurchaseRepository;
import com.example.c320.Repositories.UserRepository;
import com.example.c320.Repositories.BasketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private PaintingService paintingService;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public boolean isUserRegistered(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Compare the plain text passwords directly
            return user.getPassword().equals(password);
        }
        return false;
    }

    public User createUser(User user) {
        userRepository.save(user);
        Basket basket = new Basket();
        basket.setUserID(user.getId());
        user.setBasket(basket);
        basketRepository.save(basket);
        return userRepository.save(user);
    }
    public Optional<User> getUserByEmail(String email) { return userRepository.findByEmail(email); }

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

    public void deleteUser(String userId) {
        // Optional: Check if the user exists before attempting to delete
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));

        // Delete the users basket
        basketRepository.delete(user.getBasket());
        // Delete the user
        userRepository.delete(user);
    }

    public Purchase purchase(String userId) {
        // Retrieve the user and their basket
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        Basket basket = user.getBasket();

        if (basket.getPaintings().isEmpty()) {
            throw new IllegalStateException("Basket is empty.");
        }
        List<Painting> paintings = basket.getPaintings();
        for(Painting p: paintings){
            paintingService.deletePainting(p.getId());
        }
        // Record the purchase
        Purchase purchase = new Purchase();
        purchase.setUserID(user.getId());
        purchase.setPaintings(basket.getPaintings());
        purchase.setTotal(basket.getTotal());
        purchaseRepository.save(purchase);
        // Clear the items in the basket
        basket.setPaintings(new ArrayList<Painting>());
        // Save the cleared basket
        basketRepository.save(basket);
        // Save the user
        user.setBasket(basket);
        userRepository.save(user);

        return purchase;
    }

    // Additional methods for update and delete
}
