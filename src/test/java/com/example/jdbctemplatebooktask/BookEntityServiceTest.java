package com.example.jdbctemplatebooktask;

import com.example.jdbctemplatebooktask.Entity.BookEntity;
import com.example.jdbctemplatebooktask.Exceptions.BookNotFoundException;
import com.example.jdbctemplatebooktask.Repository.JdbcBookRepository;
import com.example.jdbctemplatebooktask.Service.BookEntityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookEntityServiceTest {

    @Mock
    private JdbcBookRepository jdbcBookRepository;

    @InjectMocks
    private BookEntityService bookEntityService;

    private BookEntity testBook1;
    private BookEntity testBook2;

    @BeforeEach
    void setUp() {
        testBook1 = BookEntity.builder().id(1L).title("Test Book 1").author("Test Author 1").publicationYear(2023).build();
        testBook2 = BookEntity.builder().id(2L).title("Test Book 2").author("Test Author 2").publicationYear(2022).build();
    }

    @Test
    void getBookById_ExistingBook_ReturnsBook() {
        when(jdbcBookRepository.findById(1L)).thenReturn(Optional.of(testBook1));

        BookEntity book = bookEntityService.getBookById(1L);

        assertEquals(testBook1, book);
    }

    @Test
    void getBookById_NonExistingBook_ThrowsBookNotFoundException() {
        when(jdbcBookRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookEntityService.getBookById(999L));
    }

    @Test
    void getAllBooks_BooksExist_ReturnsListOfBooks() {
        List<BookEntity> books = Arrays.asList(testBook1, testBook2);
        when(jdbcBookRepository.findAll()).thenReturn(books);

        List<BookEntity> allBooks = bookEntityService.getAllBooks();

        assertEquals(books, allBooks);
    }

    @Test
    void getAllBooks_NoBooksExist_ReturnsEmptyList() {
        when(jdbcBookRepository.findAll()).thenReturn(java.util.Collections.emptyList()); // or use Collections.emptyList()

        List<BookEntity> allBooks = bookEntityService.getAllBooks();

        assertTrue(allBooks.isEmpty());
    }

    @Test
    void addBook_ValidBook_ReturnsCreatedBook() {
        when(jdbcBookRepository.create(testBook1)).thenReturn(testBook1);

        BookEntity createdBook = bookEntityService.addBook(testBook1);

        assertEquals(testBook1, createdBook);
    }
    @Test
    void addBook_NullBook_ThrowsBookNotFoundException() {
        BookEntity nullBook = null;

        assertThrows(BookNotFoundException.class, () -> bookEntityService.addBook(nullBook));
    }

    @Test
    void updateBook_ExistingBook_ReturnsUpdatedBook() {
        when(jdbcBookRepository.update(testBook1)).thenReturn(testBook1);

        BookEntity updatedBook = bookEntityService.updateBook(testBook1);

        assertEquals(testBook1, updatedBook);
    }

    @Test
    void updateBook_NonExistingBook_ReturnsNull() {
        when(jdbcBookRepository.update(testBook1)).thenReturn(null);

        BookEntity updatedBook = bookEntityService.updateBook(testBook1);

        assertNull(updatedBook);
    }

    @Test
    void deleteBookById_ValidId_DeletesBook() {
        bookEntityService.deleteBookById(1L);

        verify(jdbcBookRepository, times(1)).delete(1L);
    }
}
