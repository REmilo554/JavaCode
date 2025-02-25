package com.example.jdbctemplatebooktask;

import com.example.jdbctemplatebooktask.Entity.BookEntity;
import com.example.jdbctemplatebooktask.Exceptions.BookNotFoundException;
import com.example.jdbctemplatebooktask.Repository.JdbcBookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JdbcBookRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private JdbcBookRepository jdbcBookRepository;

    private BookEntity testBook1;
    private BookEntity testBook2;

    @BeforeEach
    void setUp() {
        testBook1 = BookEntity.builder()
                .id(1L)
                .title("Test Book 1")
                .author("Test Author 1")
                .publicationYear(2023)
                .build();
        testBook2 = BookEntity.builder()
                .id(2L).title("Test Book 2")
                .author("Test Author 2")
                .publicationYear(2022)
                .build();
    }

    @Test
    void findById_ExistingBook_ReturnsBook() {
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), any(RowMapper.class))).thenReturn(testBook1);

        Optional<BookEntity> book = jdbcBookRepository.findById(1L);

        assertTrue(book.isPresent());
        assertEquals(testBook1, book.get());
    }

    @Test
    void findById_NonExistingBook_ReturnsEmpty() {
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenThrow(new EmptyResultDataAccessException(1));

        Optional<BookEntity> book = jdbcBookRepository.findById(999L);
        assertFalse(book.isPresent());
    }

    @Test
    void findAll_BooksExist_ReturnsListOfBooks() {
        List<BookEntity> books = Arrays.asList(testBook1, testBook2);

        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(books);

        List<BookEntity> allBooks = jdbcBookRepository.findAll();

        assertEquals(2, allBooks.size());
        assertEquals(books, allBooks);
    }

    @Test
    void findAll_NoBooksExist_ReturnsEmptyList() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(Collections.emptyList());

        List<BookEntity> allBooks = jdbcBookRepository.findAll();

        assertTrue(allBooks.isEmpty());
    }

    @Test
    void create_ValidBook_ReturnsCreatedBook() {
        BookEntity newBook = BookEntity.builder().title("New Book").author("New Author").publicationYear(2024).build();

        when(jdbcTemplate.update(any(PreparedStatementCreator.class), any(KeyHolder.class)))
                .thenAnswer(invocation -> {
                    KeyHolder kh = invocation.getArgument(1);
                    kh.getKeyList().add(Collections.singletonMap("book_id", 1L)); //Simulate generated key
                    return 1;
                });

        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), any(RowMapper.class))).thenReturn(newBook);

        BookEntity createdBook = jdbcBookRepository.create(newBook);

        assertNotNull(createdBook);
        assertEquals(newBook, createdBook);
    }

    @Test
    void create_DataAccessException_ThrowsBookNotFoundException() {
        BookEntity newBook = BookEntity.builder().title("New Book").author("New Author").publicationYear(2024).build();

        when(jdbcTemplate.update(any(PreparedStatementCreator.class), any(KeyHolder.class)))
                .thenThrow(new DataAccessException("Simulated exception") {});

        assertThrows(BookNotFoundException.class, () -> jdbcBookRepository.create(newBook));
    }

    @Test
    void update_ExistingBook_ReturnsUpdatedBook() {
        when(jdbcTemplate.update(anyString(), any(), any(), any(), any())).thenReturn(1);
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(testBook1);

        BookEntity updatedBook = jdbcBookRepository.update(testBook1);

        assertEquals(testBook1, updatedBook);
    }

    @Test
    void update_NonExistingBook_ThrowsBookNotFoundException() {
        when(jdbcTemplate.update(anyString(), any(), any(), any(), any())).thenReturn(0);

        assertThrows(BookNotFoundException.class, () -> jdbcBookRepository.update(testBook1));
    }

    @Test
    void update_DataAccessException_ThrowsBookNotFoundException() {
        when(jdbcTemplate.update(anyString(), any(), any(), any(), any()))
                .thenThrow(new DataAccessException("Simulated exception") {});

        assertThrows(BookNotFoundException.class, () -> jdbcBookRepository.update(testBook1));
    }

    @Test
    void delete_ExistingBook_DeletesBook() {
        when(jdbcTemplate.update(anyString(), anyLong())).thenReturn(1);

        jdbcBookRepository.delete(1L);

        verify(jdbcTemplate, times(1)).update(anyString(), anyLong());
    }

    @Test
    void delete_NonExistingBook_ThrowsBookNotFoundException() {
        when(jdbcTemplate.update(anyString(), anyLong())).thenReturn(0);

        assertThrows(BookNotFoundException.class, () -> jdbcBookRepository.delete(999L));
    }

    @Test
    void delete_DataAccessException_ThrowsBookNotFoundException() {
        when(jdbcTemplate.update(anyString(), anyLong()))
                .thenThrow(new DataAccessException("Simulated exception") {
                });

        assertThrows(BookNotFoundException.class, () -> jdbcBookRepository.delete(1L));
    }
}
