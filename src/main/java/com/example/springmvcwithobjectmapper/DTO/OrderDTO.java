package com.example.springmvcwithobjectmapper.DTO;

import com.example.springmvcwithobjectmapper.Entity.Product;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDTO {
    Long orderId;
    Long customerId;
    List<Product> products;
    LocalDate orderDate;
    String shippingAddress;
    BigDecimal totalPrice;
    String orderStatus;
}
