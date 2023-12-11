package com.example.c320.Controller;
import com.example.c320.Services.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.c320.Entities.Basket;
import java.util.List;

@RestController
@RequestMapping("/basket")
public class BasketController {

    @Autowired
    private BasketService basketService;

    @GetMapping
    public List<Basket> getAllBaskets() {
        return basketService.getAllBaskets();
    }

    @GetMapping("/{id}")
    public Basket getBasketById(@PathVariable String id) {
        return basketService.getBasketById(id);
    }

    @PostMapping
    public Basket createBasket(@RequestBody Basket basket) {
        return basketService.createBasket(basket);
    }

    // Additional endpoints for update and delete
}
