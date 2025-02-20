package com.example.springmvcwithpageable.Service;


import com.example.springmvcwithpageable.DTO.AuthorDTO;
import com.example.springmvcwithpageable.DTO.BookCreateDTO;
import com.example.springmvcwithpageable.DTO.BookDTO;
import com.example.springmvcwithpageable.Entity.AuthorEntity;
import com.example.springmvcwithpageable.Entity.BookEntity;
import com.example.springmvcwithpageable.ExceptionsHandler.BookNotFoundException;
import com.example.springmvcwithpageable.Repository.AuthorEntityRepository;
import com.example.springmvcwithpageable.Repository.BooksEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class BooksService {

    private final BooksEntityRepository booksEntityRepository;
    private final AuthorEntityRepository authorEntityRepository;

    @Transactional(readOnly = true)
    public Page<BookEntity> getAllBooks(Pageable pageable) {
        Page<BookEntity> all = booksEntityRepository.findAll(pageable);
        if (!all.hasContent()) {
            throw new BookNotFoundException("Books not found", HttpStatus.NOT_FOUND);
        }
        return all;
    }

    @Transactional(readOnly = true)
    public BookDTO getBookById(Long id) {
        if (id == null) {
            throw new BookNotFoundException("Id is null", HttpStatus.BAD_REQUEST);
        }
        Optional<BookEntity> bookEntity = booksEntityRepository.findById(id);
        if (!bookEntity.isPresent()) {
            throw new BookNotFoundException("Book not found", HttpStatus.NOT_FOUND);
        }
        AuthorDTO authorDTO = null;
        if (bookEntity.get().getAuthor() != null) {
            authorDTO = AuthorDTO.builder()
                    .id(bookEntity.get().getId())
                    .fullName(bookEntity.get().getAuthor().getFullName())
                    .build();
        }
        return BookDTO.builder()
                .id(bookEntity.get().getId())
                .title(bookEntity.get().getName())
                .author(authorDTO)
                .build();
    }

    @Transactional
    public BookEntity addBook(BookCreateDTO bookCreateDTO) {
        AuthorEntity authorEntity = authorEntityRepository
                .findById(bookCreateDTO.getAuthorId())
                .orElseThrow(() -> new BookNotFoundException("Author not found", HttpStatus.NOT_FOUND));
        BookEntity book = BookEntity.builder()
                .name(bookCreateDTO.getName())
                .author(authorEntity)
                .build();
        return booksEntityRepository.save(book);
    }

    @Transactional
    public Integer updateBook(Long id,BookCreateDTO bookCreateDTO) {
        if(id == null) {
            throw new BookNotFoundException("Id is null", HttpStatus.BAD_REQUEST);
        }
        Optional<AuthorEntity> authorEntity = authorEntityRepository.findById(bookCreateDTO.getAuthorId());
        if (!authorEntity.isPresent()) {
            throw new BookNotFoundException("Author not found", HttpStatus.NOT_FOUND);
        }
        Integer countOfUpdates = booksEntityRepository.updateBook(id, bookCreateDTO.getName(), authorEntity.get());
        if(countOfUpdates == 0) {
            throw new BookNotFoundException("Book not found", HttpStatus.NOT_FOUND);
        }
        return countOfUpdates;
    }

    @Transactional
    public Integer deleteBookById(Long id) {
        if (id == null) {
            throw new BookNotFoundException("Id is null", HttpStatus.BAD_REQUEST);
        }
        booksEntityRepository.deleteById(id);
        return 1;
    }
}
