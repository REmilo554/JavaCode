package com.example.springmvcwithobjectmapper.Repository;

import com.example.springmvcwithobjectmapper.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}