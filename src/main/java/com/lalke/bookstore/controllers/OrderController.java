package com.lalke.bookstore.controllers;

import com.lalke.bookstore.domain.Cart;
import com.lalke.bookstore.domain.Order;
import com.lalke.bookstore.services.OrderService;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    @ModelAttribute("cart")
    public Cart cart() {
        return new Cart();
    }

    @PostMapping
    public String makeOrder(@RequestParam String address, @ModelAttribute("cart") Cart cart){
        orderService.createOrder(cart, address);
        cart.clear();
        return "redirect:/";
    }

    @GetMapping
    public String showAllOrders(Model model, HtmxRequest htmxRequest, HttpServletResponse response) {
        model.addAttribute("orders", orderService.findAllOrders());
        if (htmxRequest.isHtmxRequest()) {
            response.setHeader("HX-Title", "Orders");
            return "order/ordersView :: main";
        }
        return "order/ordersView";
    }

    @PostMapping("/{id}/accept")
    public String acceptOrder(@PathVariable String id, Model model) {
        Order order = orderService.acceptOrder(id);
        model.addAttribute("order", order);
        return "order/ordersView :: orderStatusFragment";
    }
}
