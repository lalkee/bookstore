package com.lalke.bookstore.repositories;

import com.lalke.bookstore.domain.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findAllByOrderByTimestampDesc();
}
