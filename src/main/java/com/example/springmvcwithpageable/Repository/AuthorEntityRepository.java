package com.example.springmvcwithpageable.Repository;

import com.example.springmvcwithpageable.Entity.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorEntityRepository extends JpaRepository<AuthorEntity, Long> {

}
