package com.example.c320.Controller;
import com.example.c320.Services.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    @GetMapping("/id/{id}")
    public ResponseEntity<Basket> getBasketById(@PathVariable String id) {
        return basketService.getBasketById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    // Additional endpoints for update and delete
}
