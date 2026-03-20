package com.lalke.bookstore.controllers;

import com.lalke.bookstore.domain.Cart;
import com.lalke.bookstore.domain.Order;
import com.lalke.bookstore.repositories.OrderRepository;
import com.lalke.bookstore.services.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orders")
@SessionAttributes("cart")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderRepository orderRepository;

    @ModelAttribute("cart")
    public Cart cart() {
        return new Cart();
    }


    @PostMapping
    public String makeOrder(@RequestParam String address, HttpSession httpSession){
        Cart cart = (Cart) httpSession.getAttribute("cart");
        Order order = orderService.createOrder(cart, address);
        orderRepository.save(order);
        httpSession.setAttribute("cart", new Cart());
        return "redirect:/";
    }

    @GetMapping
    public String showAllOrders(Model model) {
        model.addAttribute("orders", orderService.findAllOrders());
        return "ordersView";
    }

    @PostMapping("/{id}/accept")
    public String acceptOrder(@PathVariable String id, Model model) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(Order.OrderStatus.ACCEPTED);
        orderRepository.save(order);

        model.addAttribute("order", order);
        return "ordersView :: orderStatusFragment";
    }

}
