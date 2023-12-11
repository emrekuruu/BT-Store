package com.example.c320.Services;
import com.example.c320.Entities.Basket;
import com.example.c320.Entities.User;
import com.example.c320.Repositories.UserRepository;
import com.example.c320.Repositories.BasketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BasketRepository basketRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public User createUser(User user) {
        Basket basket = new Basket();
        basket.setUser(user);
        basketRepository.save(basket);
        return userRepository.save(user);
    }

    // Additional methods for update and delete
}
