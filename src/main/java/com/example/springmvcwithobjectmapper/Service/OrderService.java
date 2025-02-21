package com.example.springmvcwithobjectmapper.Service;


import com.example.springmvcwithobjectmapper.DTO.OrderCreateDTO;
import com.example.springmvcwithobjectmapper.DTO.OrderDTO;
import com.example.springmvcwithobjectmapper.Entity.Customer;
import com.example.springmvcwithobjectmapper.Entity.Order;
import com.example.springmvcwithobjectmapper.Exceptions.CustomerNotFoundException;
import com.example.springmvcwithobjectmapper.Exceptions.OrderNotFoundException;
import com.example.springmvcwithobjectmapper.Repository.CustomerRepository;
import com.example.springmvcwithobjectmapper.Repository.OrderRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
        return convertToDTO(order);
    }

    @Transactional
    public OrderDTO createOrder(@Valid OrderCreateDTO orderCreateDTO) {
        Customer customer = customerRepository.findById(orderCreateDTO.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + orderCreateDTO.getCustomerId()));

        Order order = Order.builder()
                .customer(customer)
                .products(orderCreateDTO.getProducts())
                .orderDate(orderCreateDTO.getOrderDate())
                .shippingAddress(orderCreateDTO.getShippingAddress())
                .totalPrice(orderCreateDTO.getTotalPrice())
                .orderStatus(orderCreateDTO.getOrderStatus())
                .build();
        Order savedOrder = orderRepository.save(order);
        return convertToDTO(savedOrder);
    }

    private OrderDTO convertToDTO(Order order) {
        return OrderDTO.builder()
                .orderId(order.getOrderId())
                .customerId(order.getCustomer().getCustomerId())
                .products(order.getProducts())
                .orderDate(order.getOrderDate())
                .shippingAddress(order.getShippingAddress())
                .totalPrice(order.getTotalPrice())
                .orderStatus(order.getOrderStatus())
                .build();
    }
}
