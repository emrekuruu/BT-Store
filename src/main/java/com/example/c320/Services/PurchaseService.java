package com.example.c320.Services;
import com.example.c320.Entities.Purchase;
import com.example.c320.Repositories.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseService {

    @Autowired
    PurchaseRepository purchaseRepository;
    public Purchase createPurchase(Purchase purchase){ return purchaseRepository.save(purchase);}

}
