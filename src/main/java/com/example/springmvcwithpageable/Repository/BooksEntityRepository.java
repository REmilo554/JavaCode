package com.example.springmvcwithpageable.Repository;

import com.example.springmvcwithpageable.Entity.AuthorEntity;
import com.example.springmvcwithpageable.Entity.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Book;

@Repository
public interface BooksEntityRepository extends JpaRepository<BookEntity, Long> {

    @Modifying
    @Query("update BookEntity b set b.name=:title,b.author=:author where b.id=:id")
    int updateBook(Long id, String title, AuthorEntity author);

    Page<BookEntity> findAll(Pageable pageable);
}
