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

        if (!restaurant.getOwner().getEmail().equals(ownerEmail)) {
            throw new SecurityException("You are not the owner of this restaurant");
        }

        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);
        product.setRestaurant(restaurant);

        return productRepo.save(product);
    }

    private Product getOwnedProduct(Long productId, Long restaurantId, String ownerEmail) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Product not found: " + productId));

        if (!product.getRestaurant().getId().equals(restaurantId)) {
            throw new SecurityException("Product does not belong to this restaurant");
        }

        if (!product.getRestaurant().getOwner().getEmail().equals(ownerEmail)) {
            throw new SecurityException("You are not the owner of this restaurant");
        }

        return product;
    }

    public Product findByIdForOwner(Long productId, Long restaurantId, String ownerEmail) {
        return getOwnedProduct(productId, restaurantId, ownerEmail);
    }

    public void deleteProduct(Long productId, Long restaurantId, String ownerEmail) {
        Product product = getOwnedProduct(productId, restaurantId, ownerEmail);
        productRepo.delete(product);
    }

    public Product updateProduct(Long productId, Long restaurantId, String name,
                                 BigDecimal price, String description, String ownerEmail) {

        Product product = getOwnedProduct(productId, restaurantId, ownerEmail);

        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);

        return productRepo.save(product);
    }
}