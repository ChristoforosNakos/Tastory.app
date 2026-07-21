package gr.tastory.aueb.service;

import gr.tastory.aueb.model.Restaurant;
import gr.tastory.aueb.model.User;
import gr.tastory.aueb.repository.ProductRepo;
import gr.tastory.aueb.repository.RestaurantRepo;
import org.junit.jupiter.api.Test;
import gr.tastory.aueb.model.Product;

import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductServiceTest {

    @Test
    void addProduct_whenWrongOwner_throwsSecurityException() {

        ProductRepo productRepo = mock(ProductRepo.class);
        RestaurantRepo restaurantRepo = mock(RestaurantRepo.class);

        ProductService service = new ProductService(productRepo, restaurantRepo);

        User owner = new User();
        owner.setEmail("test@test.com");

        Restaurant restaurant = new Restaurant();
        restaurant.setOwner(owner);

        when(restaurantRepo.findById(1L)).thenReturn(Optional.of(restaurant));

        assertThrows(SecurityException.class, () -> {
            service.addProduct(1L, "Pizza", new BigDecimal("9.99"),
                    "Tasty", "attacker@test.com");
        });
    }
    @Test
    void addProduct_whenCorrectOwner_savesProduct() {

        // ARRANGE
        ProductRepo productRepo = mock(ProductRepo.class);
        RestaurantRepo restaurantRepo = mock(RestaurantRepo.class);
        ProductService service = new ProductService(productRepo, restaurantRepo);

        User owner = new User();
        owner.setEmail("test@test.com");

        Restaurant restaurant = new Restaurant();
        restaurant.setOwner(owner);

        when(restaurantRepo.findById(1L)).thenReturn(Optional.of(restaurant));

        // ACT — ΣΩΣΤΟΣ owner (ίδιο email)
        service.addProduct(1L, "Pizza", new BigDecimal("9.99"),
                "Tasty", "test@test.com");

        // ASSERT — σιγουρεύουμε ότι κλήθηκε το save με κάποιο Product
        verify(productRepo).save(any(Product.class));
    }
}