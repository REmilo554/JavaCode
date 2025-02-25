package com.example.jdbctemplatebooktask.Repository;

import com.example.jdbctemplatebooktask.Entity.BookEntity;
import com.example.jdbctemplatebooktask.Exceptions.BookNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcBookRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcBookRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<BookEntity> findById(Long id) {
        String sql = "SELECT * FROM books WHERE book_id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new Object[]{id}, new BookRowMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<BookEntity> findAll() {
        String sql = "SELECT book_id, title, author, publication_year FROM books";
        return jdbcTemplate.query(sql, new BookRowMapper());
    }

    public BookEntity create(BookEntity book) {
        String sql = "INSERT INTO books (title, author, publication_year) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"book_id"});
                ps.setString(1, book.getTitle());
                ps.setString(2, book.getAuthor());
                ps.setInt(3, book.getPublicationYear());
                return ps;
            }, keyHolder);
            if (keyHolder.getKey() != null) {
                Long generatedId = keyHolder.getKey().longValue();
                return findById(generatedId).orElseThrow(() -> new BookNotFoundException("Book not found after create"));
            } else {
                throw new BookNotFoundException("Failed to retrieve generated ID");
            }
        } catch (DataAccessException e) {
            throw new BookNotFoundException("Failed to create book");
        }
    }

    public BookEntity update(BookEntity book) {
        String sql = "UPDATE books set title = ?, author = ?, publication_year = ? WHERE book_id = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql, book.getTitle(), book.getAuthor(), book.getPublicationYear(), book.getId());

            if (rowsAffected > 0) {
                return findById(book.getId()).orElseThrow(() -> new BookNotFoundException("Book not found after update"));
            } else {
                throw new BookNotFoundException("Book not found for update");
            }
        } catch (DataAccessException e) {
            throw new BookNotFoundException("Failed to update book");
        }
    }

    public void delete(Long id) {
        String sql = "DELETE FROM books WHERE book_id = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql, id);
            if (rowsAffected == 0) {
                throw new BookNotFoundException("Book not found for delete");
            }

        } catch (DataAccessException e) {
            throw new BookNotFoundException("Failed to delete book");
        }
    }


    private static class BookRowMapper implements RowMapper<BookEntity> {
        @Override
        public BookEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return BookEntity.builder()
                    .id(rs.getLong("book_id"))
                    .title(rs.getString("title"))
                    .author(rs.getString("author"))
                    .publicationYear(rs.getInt("publication_year"))
                    .build();
        }
    }
}
