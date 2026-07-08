package gr.tastory.aueb.config;

import gr.tastory.aueb.model.Product;
import gr.tastory.aueb.model.Restaurant;
import gr.tastory.aueb.model.Role;
import gr.tastory.aueb.model.User;
import gr.tastory.aueb.repository.ProductRepo;
import gr.tastory.aueb.repository.RestaurantRepo;
import gr.tastory.aueb.repository.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepo userRepo;
    private final RestaurantRepo restaurantRepo;
    private final ProductRepo productRepo;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepo userRepo, RestaurantRepo restaurantRepo,
                      ProductRepo productRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.restaurantRepo = restaurantRepo;
        this.productRepo = productRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        // --- Admin ---
        if (userRepo.findByEmail("admin@tastory.gr").isEmpty()) {
            User admin = new User();
            admin.setEmail("admin@tastory.gr");
            admin.setName("Admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            userRepo.save(admin);
            System.out.println(">>> Admin user created: admin@tastory.gr");
        }

        // --- Εστιατόρια + πιάτα (μόνο αν δεν υπάρχουν ήδη) ---
        if (userRepo.findByEmail("owner@tastory.gr").isEmpty()) {

            User owner = new User();
            owner.setEmail("owner@tastory.gr");
            owner.setName("Owner");
            owner.setPassword(passwordEncoder.encode("owner123"));
            owner.setRole(Role.RESTAURANT_OWNER);
            userRepo.save(owner);

            seed(owner, "Θημαρί", "Πλάκα, Αθήνα",
                    new String[]{"Μουσακάς", "Παστίτσιο", "Χωριάτικη σαλάτα"},
                    new double[]{9.50, 8.50, 6.00});

            seed(owner, "La Pasta", "Γλυφάδα, Αθήνα",
                    new String[]{"Spaghetti Carbonara", "Πίτσα Μαργαρίτα", "Lasagna"},
                    new double[]{8.00, 8.50, 9.00});

            seed(owner, "Meze House", "Κολωνάκι, Αθήνα",
                    new String[]{"Τζατζίκι", "Κεφτεδάκια", "Σουβλάκι χοιρινό"},
                    new double[]{4.00, 6.50, 3.50});

            seed(owner, "Ψαροταβέρνα", "Πειραιάς",
                    new String[]{"Τσιπούρα", "Καλαμαράκια", "Γαρίδες σαγανάκι"},
                    new double[]{12.00, 9.50, 11.00});

            seed(owner, "Green Bowl", "Χαλάνδρι",
                    new String[]{"Caesar salad", "Poke bowl", "Smoothie"},
                    new double[]{7.50, 9.00, 5.00});

            seed(owner, "Kafeneio", "Θεσσαλονίκη",
                    new String[]{"Ελληνικός καφές", "Μπουγάτσα", "Λουκουμάδες"},
                    new double[]{2.00, 3.50, 4.50});

            System.out.println(">>> Seed data: 6 εστιατόρια με πιάτα προστέθηκαν");
        }
    }

    private void seed(User owner, String name, String address,
                      String[] products, double[] prices) {
        Restaurant r = new Restaurant();
        r.setName(name);
        r.setAddress(address);
        r.setOwner(owner);
        restaurantRepo.save(r);

        for (int i = 0; i < products.length; i++) {
            Product p = new Product();
            p.setName(products[i]);
            p.setPrice(BigDecimal.valueOf(prices[i]));
            p.setRestaurant(r);
            productRepo.save(p);
        }
    }
}