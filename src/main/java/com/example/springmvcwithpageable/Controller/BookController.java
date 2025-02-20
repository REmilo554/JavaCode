package com.example.springmvcwithpageable.Controller;


import com.example.springmvcwithpageable.DTO.BookCreateDTO;
import com.example.springmvcwithpageable.DTO.BookDTO;
import com.example.springmvcwithpageable.Entity.BookEntity;
import com.example.springmvcwithpageable.Service.BooksService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
@AllArgsConstructor
public class BookController {

    private final BooksService booksService;

    @GetMapping("/all")
    public ResponseEntity<Page<BookEntity>> getAllBooks(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "5") int size) {
        return new ResponseEntity<>(booksService.getAllBooks(PageRequest.of(page, size)), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> findBookById(@PathVariable Long id) {
        return new ResponseEntity<>(booksService.getBookById(id), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<BookEntity> addBook(@RequestBody BookCreateDTO book) {
        return new ResponseEntity<>(booksService.addBook(book), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Integer> updateBook(@PathVariable Long id, @RequestBody BookCreateDTO book) {
        System.out.println(id);
        return new ResponseEntity<>(booksService.updateBook(id,book), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Integer> deleteBook(@PathVariable Long id) {
        return new ResponseEntity<>(booksService.deleteBookById(id), HttpStatus.OK);
    }

}
