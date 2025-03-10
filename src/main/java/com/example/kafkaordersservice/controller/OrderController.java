package com.example.kafkaordersservice.controller;


import com.example.dto.OrderDTO;
import com.example.kafkaordersservice.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/new")
    public ResponseEntity<String> newOrder(@RequestBody OrderDTO order) {
        String result = orderService.createNewOrder(order);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
