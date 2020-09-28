package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookRepository<T> implements ProjectRepository<Book>, ApplicationContextAware {

    private final Logger logger = Logger.getLogger(BookRepository.class);
    private ApplicationContext context;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public BookRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public List<Book> retrieveAll() {
        List<Book> books = jdbcTemplate.query("SELECT * FROM books", (ResultSet rs, int rowNum) -> {
            Book book = new Book();
            book.setId(rs.getInt("id"));
            book.setAuthor(rs.getString("author"));
            book.setTitle(rs.getString("title"));
            book.setSize(rs.getInt("size"));
            return book;
        });
        return new ArrayList<>(books);
    }

    @Override
    public void store(Book book) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("author", book.getAuthor());
        parameterSource.addValue("title", book.getTitle());
        parameterSource.addValue("size", book.getSize());
        jdbcTemplate.update("INSERT INTO books(author,title,size) VALUES(:author, :title, :size)", parameterSource);
        logger.info("store new book " + book);
    }

    @Override
    public boolean removeItemByParameter(final String itemParameterValue, String parameterName) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(parameterName, itemParameterValue);
        jdbcTemplate.update("DELETE FROM books WHERE  " + parameterName + " REGEXP :" + parameterName, parameterSource);
        return true;
    }

    @Override
    public List<Book> getAllItemsBySearchParam(String searchParam) {
        if (searchParam.isEmpty()) {
            return retrieveAll();
        }
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("param", searchParam);
        return jdbcTemplate.query("SELECT * FROM books WHERE author REGEXP :param" +
                " OR title REGEXP :param" +
                " OR size REGEXP :param" +
                " OR id REGEXP :param", parameterSource, (ResultSet rs, int rowNum) -> {
            Book book = new Book();
            book.setId(rs.getInt("id"));
            book.setAuthor(rs.getString("author"));
            book.setTitle(rs.getString("title"));
            book.setSize(rs.getInt("size"));
            return book;
        });
    }
}
