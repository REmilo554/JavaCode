package com.example.springmvcwithpageable;

import com.example.springmvcwithpageable.DTO.BookCreateDTO;
import com.example.springmvcwithpageable.DTO.BookDTO;
import com.example.springmvcwithpageable.Entity.AuthorEntity;
import com.example.springmvcwithpageable.Entity.BookEntity;
import com.example.springmvcwithpageable.ExceptionsHandler.BookNotFoundException;
import com.example.springmvcwithpageable.Repository.AuthorEntityRepository;
import com.example.springmvcwithpageable.Repository.BooksEntityRepository;
import com.example.springmvcwithpageable.Service.BooksService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BooksEntityRepository booksEntityRepository;

    @Mock
    private AuthorEntityRepository authorEntityRepository;

    @InjectMocks
    private BooksService booksService;

    private BookEntity bookEntity;
    private AuthorEntity authorEntity;
    private BookCreateDTO bookCreateDTO;

    @BeforeEach
    void setUp() {
        authorEntity = AuthorEntity.builder()
                .id(1L)
                .fullName("Test Author")
                .books(Collections.emptyList())
                .build();

        bookEntity = BookEntity.builder()
                .id(1L)
                .name("Test Book")
                .author(authorEntity)
                .build();

        bookCreateDTO = BookCreateDTO.builder()
                .name("New Book")
                .authorId(1L)
                .build();
    }

    @Test
    void testGetAllBooks_shouldReturnPageOfBooks() {
        Pageable pageable = PageRequest.of(0, 10);
        List<BookEntity> bookList = Collections.singletonList(bookEntity);
        Page<BookEntity> bookPage = new PageImpl<>(bookList, pageable, bookList.size());

        when(booksEntityRepository.findAll(pageable)).thenReturn(bookPage);

        Page<BookEntity> result = booksService.getAllBooks(pageable);

        assertEquals(1, result.getContent().size());
        assertEquals("Test Book", result.getContent().get(0).getName());
        verify(booksEntityRepository, times(1)).findAll(pageable);
    }

    @Test
    void testGetAllBooks_shouldThrowWhenBooksNotFound() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<BookEntity> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(booksEntityRepository.findAll(pageable)).thenReturn(emptyPage);

        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> booksService.getAllBooks(pageable));

        assertEquals("Books not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void testGetBookById_shouldReturnBookDTO() {
        when(booksEntityRepository.findById(1L)).thenReturn(Optional.of(bookEntity));

        BookDTO result = booksService.getBookById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Test Book", result.getTitle());
        assertNotNull(result.getAuthor());
        assertEquals(1L, result.getAuthor().getId());
        assertEquals("Test Author", result.getAuthor().getFullName());
        verify(booksEntityRepository, times(1)).findById(1L);
    }

    @Test
    void testGetBookById_shouldThrowWhenIdIsNull() {
        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> booksService.getBookById(null));

        assertEquals("Id is null", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testGetBookById_shouldThrowWhenBookNotFound() {
        when(booksEntityRepository.findById(1L)).thenReturn(Optional.empty());

        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> booksService.getBookById(1L));

        assertEquals("Book not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(booksEntityRepository, times(1)).findById(1L);
    }

    @Test
    void testAddBook_shouldReturnBookEntity() {
        when(authorEntityRepository.findById(1L)).thenReturn(Optional.of(authorEntity));

        BookEntity newBookEntity = BookEntity.builder()
                .id(2L)
                .name(bookCreateDTO.getName())
                .author(authorEntity)
                .build();

        when(booksEntityRepository.save(any(BookEntity.class))).thenReturn(newBookEntity);

        BookEntity result = booksService.addBook(bookCreateDTO);

        assertEquals("New Book", result.getName());
        assertEquals(authorEntity, result.getAuthor());
        verify(authorEntityRepository, times(1)).findById(1L);
        verify(booksEntityRepository, times(1)).save(any(BookEntity.class));
    }

    @Test
    void testAddBook_shouldThrowWhenAuthorNotFound() {
        when(authorEntityRepository.findById(1L)).thenReturn(Optional.empty());

        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> booksService.addBook(bookCreateDTO));

        assertEquals("Author not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(authorEntityRepository, times(1)).findById(1L);
        verify(booksEntityRepository, never()).save(any(BookEntity.class));
    }

    @Test
    void testUpdateBook_shouldReturnOne() {
        when(authorEntityRepository.findById(bookCreateDTO.getAuthorId())).thenReturn(Optional.of(authorEntity));
        when(booksEntityRepository.updateBook(1L,bookCreateDTO.getName(),authorEntity)).thenReturn(1);

        Integer result = booksService.updateBook(1L,bookCreateDTO);
        assertEquals(1,result);
    }
    @Test
    void testUpdateBook_shouldThrowWhenBookNotFound() {
        when(authorEntityRepository.findById(bookCreateDTO.getAuthorId())).thenReturn(Optional.of(authorEntity));
        when(booksEntityRepository.updateBook(1L,bookCreateDTO.getName(),authorEntity)).thenReturn(0);
        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> booksService.updateBook(1L,bookCreateDTO));

        assertEquals("Book not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }
    @Test
    void testUpdateBook_shouldThrowWhenAuthorNotFound() {
        when(authorEntityRepository.findById(bookCreateDTO.getAuthorId())).thenReturn(Optional.empty());
        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> booksService.updateBook(1L,bookCreateDTO));

        assertEquals("Author not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }
    @Test
    void testUpdateBook_shouldThrowWhenIdIsNull() {
        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> booksService.updateBook(null,bookCreateDTO));

        assertEquals("Id is null", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testDeleteBookById_shouldDeleteBook() {
        Long bookId = 1L;

        Integer result = booksService.deleteBookById(bookId);

        assertEquals(1, result);
        verify(booksEntityRepository, times(1)).deleteById(bookId);
    }

    @Test
    void testDeleteBookById_shouldThrowWhenIdIsNull() {
        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> booksService.deleteBookById(null));

        assertEquals("Id is null", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(booksEntityRepository, never()).deleteById(any());
    }
}
