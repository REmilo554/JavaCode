package com.example.jdbctemplatebooktask.Service;

import com.example.jdbctemplatebooktask.Entity.BookEntity;
import com.example.jdbctemplatebooktask.Exceptions.BookNotFoundException;
import com.example.jdbctemplatebooktask.Repository.JdbcBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookEntityService {

    private final JdbcBookRepository jdbcBookRepository;

    @Autowired
    public BookEntityService(JdbcBookRepository jdbcBookRepository) {
        this.jdbcBookRepository = jdbcBookRepository;
    }

    public BookEntity getBookById(Long id) {
        Optional<BookEntity> byId = jdbcBookRepository.findById(id);
        return byId.orElseThrow(() -> new BookNotFoundException("Book not found"));
    }

    public List<BookEntity> getAllBooks() {
        return jdbcBookRepository.findAll();
    }

    public BookEntity addBook(BookEntity book) {
        if(book == null){
            throw new BookNotFoundException("Book is null");
        }
        return jdbcBookRepository.create(book);
    }

    public BookEntity updateBook(BookEntity book) {
        return jdbcBookRepository.update(book);
    }

    public void deleteBookById(Long id) {
        if(id == null){
            throw new BookNotFoundException("Book is null");
        }
        jdbcBookRepository.delete(id);
    }
}
