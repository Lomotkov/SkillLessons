package org.example.web.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BookRepository<T> implements ProjectRepository<Book> {

    private final Logger logger = Logger.getLogger(BookRepository.class);
    private final List<Book> repo = new ArrayList<>();

    @Override
    public List<Book> retriveAll() {
        return new ArrayList<>(repo);
    }

    @Override
    public void store(Book book) {
        book.setId(String.valueOf(book.hashCode()));
        repo.add(book);
        logger.info("store new book " + book);
    }

    @Override
    public boolean removeItemById(String bookIdToRemove) {
        for (Book book : repo) {
            if(book.getId().equals(bookIdToRemove)) {
                repo.remove(book);
                return true;
            }
        }
        return false;
    }
}
