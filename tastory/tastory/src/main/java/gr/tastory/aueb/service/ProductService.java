package gr.tastory.aueb.service;

import gr.tastory.aueb.model.Product;
import gr.tastory.aueb.model.Restaurant;
import gr.tastory.aueb.repository.ProductRepo;
import gr.tastory.aueb.repository.RestaurantRepo;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepo productRepo;
    private final RestaurantRepo restaurantRepo;

    public ProductService(ProductRepo productRepo, RestaurantRepo restaurantRepo) {
        this.productRepo = productRepo;
        this.restaurantRepo = restaurantRepo;
    }

    public List<Product> findByRestaurant(Long restaurantId) {
        return productRepo.findByRestaurantId(restaurantId);
    }

    public Product addProduct(Long restaurantId, String name, BigDecimal price,
                              String description, String ownerEmail) {

        Restaurant restaurant = restaurantRepo.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Restaurant not found: " + restaurantId));

        //Έλεγχος ιδιοκτησίας: είναι ο συνδεδεμένος χρήστης ο owner ΑΥΤΟΥ του εστιατορίου;
        if (!restaurant.getOwner().getEmail().equals(ownerEmail)) {
            throw new SecurityException(
                    "You are not the owner of this restaurant");
        }

        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);
        product.setRestaurant(restaurant);

        return productRepo.save(product);
    }

    // DELETE — σβήνει προϊόν, με έλεγχο ιδιοκτησίας
    public void deleteProduct(Long productId, String ownerEmail) {

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Product not found: " + productId));

        // πρώτα η ταυτότητα
        if (!product.getRestaurant().getOwner().getEmail().equals(ownerEmail)) {
            throw new SecurityException("You are not the owner of this restaurant");
        }

        productRepo.delete(product);
    }

    // UPDATE:βρίσκει ένα προϊόν (για να γεμίσει τη φόρμα edit)
    public Product findById(Long productId) {
        return productRepo.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Product not found: " + productId));
    }

    //UPDATE:αποθηκεύει τις αλλαγές, με έλεγχο ιδιοκτησίας
    public Product updateProduct(Long productId, String name, BigDecimal price,
                                 String description, String ownerEmail) {

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Product not found: " + productId));

        //έλεγχος ιδιοκτησίας πριν αλλάξουμε οτιδήποτε
        if (!product.getRestaurant().getOwner().getEmail().equals(ownerEmail)) {
            throw new SecurityException("You are not the owner of this restaurant");
        }

        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);

        return productRepo.save(product);
    }
}
