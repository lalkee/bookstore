package com.lalke.bookstore.repositories;

import com.lalke.bookstore.domain.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(@NotBlank(message = "Email is required") String email);
}
