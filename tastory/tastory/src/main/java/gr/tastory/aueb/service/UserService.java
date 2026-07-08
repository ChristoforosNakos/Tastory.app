package gr.tastory.aueb.service;

import gr.tastory.aueb.model.Role;
import gr.tastory.aueb.model.User;
import gr.tastory.aueb.repository.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String email, String name, String password, Role role) {
        // 1. Έλεγχος αν το email υπάρχει ήδη
        if (userRepo.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already in use: " + email);
        }

        // 2. Δημιουργία νέου User
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        user.setRole(role);

        // 3. Αποθήκευση στη βάση
        return userRepo.save(user);
    }

    public List<User> findAllUsers() {
        return userRepo.findAll();
    }

    public void promoteToOwner(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        user.setRole(Role.RESTAURANT_OWNER);
        userRepo.save(user);
    }


}