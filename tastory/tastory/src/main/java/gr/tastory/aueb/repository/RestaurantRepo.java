package gr.tastory.aueb.repository;

import gr.tastory.aueb.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepo extends JpaRepository<Restaurant, Long> {

}
