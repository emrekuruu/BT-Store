package com.example.c320.Services;
import com.example.c320.Entities.Basket;
import com.example.c320.Repositories.BasketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BasketService {

    @Autowired
    private BasketRepository basketRepository;
    public List<Basket> getAllBaskets() {
        return basketRepository.findAll();
    }
    public Optional<Basket> getBasketById(String id) {
        return basketRepository.findById(id);
    }
    public Basket createBasket(Basket basket) {
        return basketRepository.save(basket);
    }

    // Additional methods for update and delete
}
