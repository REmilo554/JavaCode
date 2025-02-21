package com.example.springmvcwithobjectmapper.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    Long productId;

    @NotBlank(message = "Name is mandatory")
    @Column(name = "name")
    String name;

    @Column(name = "description")
    String description;

    @Column(name = "price")
    @NotNull(message = "Price is mandatory")
    BigDecimal price;

    @Column(name = "quantity_in_stock")
    @NotNull(message = "Quantity in stock is mandatory")
    Integer quantityInStock;
}
