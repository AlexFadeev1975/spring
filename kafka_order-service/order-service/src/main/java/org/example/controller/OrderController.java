package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Order;
import org.example.services.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders/api/v1")
@Slf4j
@RequiredArgsConstructor
public class OrderController {


    private final OrderService orderService;

    @PostMapping("/get")
    public Order getOrder(@RequestBody Order order) {

        log.info("Order: {} is received", order);

        orderService.add(order);


        return order;
    }
}
