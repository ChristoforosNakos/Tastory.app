package gr.tastory.aueb.repository;

import gr.tastory.aueb.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order,Long> {

          List<Order> findByRestaurantId(Long restaurantId);

          List<Order> findByUserId(Long userId);
}
