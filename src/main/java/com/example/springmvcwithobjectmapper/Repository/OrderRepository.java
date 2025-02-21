package com.example.springmvcwithobjectmapper.Repository;

import com.example.springmvcwithobjectmapper.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}