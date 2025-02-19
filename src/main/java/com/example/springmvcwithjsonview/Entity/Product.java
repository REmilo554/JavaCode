package com.example.springmvcwithjsonview.Entity;

import com.example.springmvcwithjsonview.View.UserDetails;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Table(name = "products")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    @JsonView(UserDetails.class)
    Long id;
    @Column(name = "name", nullable = false)
    @JsonView(UserDetails.class)
    String name;
    @Column(name = "price", nullable = false)
    @JsonView(UserDetails.class)
    BigDecimal price;
    @Column(name = "amount", nullable = false)
    BigDecimal amount;
}
