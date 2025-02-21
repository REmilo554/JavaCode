package com.example.springmvcwithobjectmapper.Controller;

import com.example.springmvcwithobjectmapper.DTO.OrderCreateDTO;
import com.example.springmvcwithobjectmapper.Service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createOrder(@Valid @RequestBody String orderCreateDTO) throws JsonProcessingException {
        return new ResponseEntity<>(objectMapper
                .writeValueAsString(orderService
                        .createOrder(objectMapper
                                .readValue(orderCreateDTO, OrderCreateDTO.class))), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getOrderById(@PathVariable Long id) throws JsonProcessingException {
        return new ResponseEntity<>(objectMapper.writeValueAsString(orderService.getOrderById(id)), HttpStatus.OK);
    }
}
