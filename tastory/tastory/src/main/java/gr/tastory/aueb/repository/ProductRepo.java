package gr.tastory.aueb.repository;

import gr.tastory.aueb.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {

    List<Product> findByRestaurantId(Long restaurantId);
}


