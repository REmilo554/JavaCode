package com.example.jdbctemplatebooktask.Controller;


import com.example.jdbctemplatebooktask.Entity.BookEntity;
import com.example.jdbctemplatebooktask.Service.BookEntityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookEntityController {

    private final BookEntityService bookEntityService;

    @Autowired
    public BookEntityController(BookEntityService bookEntityService) {
        this.bookEntityService = bookEntityService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookEntity> getBookById(@PathVariable Long id) {
        return new ResponseEntity<>(bookEntityService.getBookById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<BookEntity>> getAllBooks() {
        return new ResponseEntity<>(bookEntityService.getAllBooks(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable Long id) {
        bookEntityService.deleteBookById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<BookEntity> addBook(@Valid @RequestBody BookEntity book) {
        return new ResponseEntity<>(bookEntityService.addBook(book), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<BookEntity> updateBook(@Valid @RequestBody BookEntity book) {
        return new ResponseEntity<>(bookEntityService.updateBook(book), HttpStatus.OK);
    }
}
