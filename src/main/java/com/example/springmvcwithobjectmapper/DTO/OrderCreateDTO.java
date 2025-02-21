package com.example.springmvcwithobjectmapper.DTO;

import com.example.springmvcwithobjectmapper.Entity.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class OrderCreateDTO {

    @NotNull(message = "Customer ID is mandatory")
    Long customerId;

    List<Product> products;

    @NotNull(message = "Order date is mandatory")
    LocalDate orderDate;

    @NotBlank(message = "Shipping address is mandatory")
    String shippingAddress;

    @NotNull(message = "Total price is mandatory")
    BigDecimal totalPrice;

    @NotBlank(message = "Order status is mandatory")
    String orderStatus;
}
