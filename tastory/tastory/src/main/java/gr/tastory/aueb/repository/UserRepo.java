package gr.tastory.aueb.repository;

import gr.tastory.aueb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepo extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);
}
