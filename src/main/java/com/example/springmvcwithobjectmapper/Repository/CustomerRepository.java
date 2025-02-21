package com.example.springmvcwithobjectmapper.Repository;

import com.example.springmvcwithobjectmapper.Entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}