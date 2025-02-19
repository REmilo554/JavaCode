package com.example.springmvcwithjsonview.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Table(name = "products")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue
    @Column(name = "product_id")
    UUID id;
    @Column(name = "name", nullable = false)
    String name;
    @Column(name = "price", nullable = false)
    BigDecimal price;
    @Column(name = "amount", nullable = false)
    BigDecimal amount;
}
