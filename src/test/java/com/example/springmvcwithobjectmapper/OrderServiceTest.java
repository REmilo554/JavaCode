package com.example.springmvcwithobjectmapper;

import com.example.springmvcwithobjectmapper.DTO.OrderCreateDTO;
import com.example.springmvcwithobjectmapper.DTO.OrderDTO;
import com.example.springmvcwithobjectmapper.Entity.Customer;
import com.example.springmvcwithobjectmapper.Entity.Order;
import com.example.springmvcwithobjectmapper.Exceptions.CustomerNotFoundException;
import com.example.springmvcwithobjectmapper.Exceptions.OrderNotFoundException;
import com.example.springmvcwithobjectmapper.Repository.CustomerRepository;
import com.example.springmvcwithobjectmapper.Repository.OrderRepository;
import com.example.springmvcwithobjectmapper.Service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private OrderService orderService;

    private Customer testCustomer;
    private Order testOrder;
    private OrderCreateDTO testOrderCreateDTO;

    @BeforeEach
    void setUp() {
        testCustomer = Customer.builder()
                .customerId(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .contactNumber("+15551234567")
                .build();

        testOrder = Order.builder()
                .orderId(1L)
                .customer(testCustomer)
                .products(Collections.emptyList())
                .orderDate(LocalDate.now())
                .shippingAddress("123 Main St, Anytown")
                .totalPrice(BigDecimal.valueOf(100.00))
                .orderStatus("CREATED")
                .build();

        testOrderCreateDTO = OrderCreateDTO.builder()
                .customerId(1L)
                .products(Collections.emptyList())
                .orderDate(LocalDate.now())
                .shippingAddress("123 Main St, Anytown")
                .totalPrice(BigDecimal.valueOf(100.00))
                .orderStatus("CREATED")
                .build();
    }

    @Test
    void getOrderById_ExistingOrderId_ReturnsOrderDTO() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        OrderDTO orderDTO = orderService.getOrderById(1L);

        assertNotNull(orderDTO);
        assertEquals(testOrder.getOrderId(), orderDTO.getOrderId());
        assertEquals(testOrder.getCustomer().getCustomerId(), orderDTO.getCustomerId());
    }

    @Test
    void getOrderById_NonExistingOrderId_ThrowsOrderNotFoundException() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(1L));
    }

    @Test
    void createOrder_ValidOrderCreateDTO_ReturnsOrderDTO() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        OrderDTO orderDTO = orderService.createOrder(testOrderCreateDTO);

        assertNotNull(orderDTO);
        assertEquals(testOrder.getOrderId(), orderDTO.getOrderId());
        assertEquals(testOrder.getCustomer().getCustomerId(), orderDTO.getCustomerId());
    }

    @Test
    void createOrder_NonExistingCustomerId_ThrowsCustomerNotFoundException() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> orderService.createOrder(testOrderCreateDTO));
    }
}
