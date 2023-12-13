package com.example.c320.Services;
import com.example.c320.Entities.Purchase;
import com.example.c320.Repositories.PurchaseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PurchaseServiceTest {

    @Mock
    private PurchaseRepository purchaseRepository;

    @InjectMocks
    private PurchaseService purchaseService;

    @Test
    public void createPurchase_Success() {
        // Arrange
        Purchase purchase = new Purchase();
        purchase.setId("p123");
        purchase.setTotal(1000.0);

        // Assume the purchase object is properly set up with user and paintings
        given(purchaseRepository.save(purchase)).willReturn(purchase);
        // Act
        Purchase savedPurchase = purchaseService.createPurchase(purchase);
        // Assert
        assertNotNull(savedPurchase, "Saved purchase should not be null");
        assertEquals(purchase.getId(), savedPurchase.getId(), "Purchase ID should match");
        assertEquals(purchase.getTotal(), savedPurchase.getTotal(), "Purchase total should match");
    }

    // Additional tests can be written for failure cases or validation logic if present
}
