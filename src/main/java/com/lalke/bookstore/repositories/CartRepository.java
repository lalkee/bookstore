package com.lalke.bookstore.repositories;

import com.lalke.bookstore.domain.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartRepository extends MongoRepository<Cart, String> {
}
