package com.example.springmvcwithobjectmapper.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDTO {

    @NotBlank(message = "Name is mandatory")
    String name;

    String description;

    @NotNull(message = "Price is mandatory")
    BigDecimal price;

    @NotNull(message = "Quantity in stock is mandatory")
    Integer quantityInStock;
}
