package com.lalke.bookstore.security;

import com.lalke.bookstore.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends MongoRepository<VerificationToken, String> {
    Optional<VerificationToken> findByToken(String token);

    void deleteByUser(User user);
}
