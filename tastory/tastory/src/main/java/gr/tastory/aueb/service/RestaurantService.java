package gr.tastory.aueb.service;

import gr.tastory.aueb.model.Restaurant;
import gr.tastory.aueb.model.User;
import gr.tastory.aueb.repository.RestaurantRepo;
import gr.tastory.aueb.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    private final RestaurantRepo restaurantRepository;
    private final UserRepo userRepo;

    public RestaurantService(RestaurantRepo restaurantRepository, UserRepo userRepo) {
        this.restaurantRepository = restaurantRepository;
        this.userRepo = userRepo;
    }

    public Restaurant createRestaurant(String name, String address, String ownerEmail) {
        // 1. Βρες τον owner στη βάση
        User owner = userRepo.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("User not found: " + ownerEmail));

        // 2. Δημιούργησε το εστιατόριο και σύνδεσέ το με τον owner
        Restaurant restaurant = new Restaurant();
        restaurant.setName(name);
        restaurant.setAddress(address);
        restaurant.setOwner(owner);

        // 3. Αποθήκευση
        return restaurantRepository.save(restaurant);
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }
}

