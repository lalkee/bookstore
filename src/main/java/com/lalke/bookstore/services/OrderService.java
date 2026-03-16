package com.lalke.bookstore.services;

import com.lalke.bookstore.domain.Cart;
import com.lalke.bookstore.domain.CartItem;
import com.lalke.bookstore.domain.Order;
import com.lalke.bookstore.domain.OrderItem;
import com.lalke.bookstore.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public Order createOrder(Cart cart, String address) {
        List<OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> OrderItem.builder()
                        .bookId(cartItem.getBookId())
                        .title(cartItem.getTitle())
                        .price(cartItem.getPrice())
                        .quantity(cartItem.getQuantity())
                        .build()
                ).toList();

        BigDecimal totalPrice = orderItems.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return Order.builder()
                .items(orderItems)
                .totalPrice(totalPrice)
                .address(address)
                .timestamp(LocalDateTime.now())
                .status(Order.OrderStatus.PENDING)
                .build();
    }

    public List<Order> findAllOrders() {
        return orderRepository.findAllByOrderByTimestampDesc();
    }
}
