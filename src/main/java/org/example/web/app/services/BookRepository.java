package org.example.web.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BookRepository<T> implements ProjectRepository<Book>, ApplicationContextAware {

    private final Logger logger = Logger.getLogger(BookRepository.class);
    private final List<Book> repo = new ArrayList<>();
    private ApplicationContext context;


    @Override
    public List<Book> retrieveAll() {
        return new ArrayList<>(repo);
    }

    @Override
    public void store(Book book) {
        book.setId(context.getBean(IdProvider.class).provideId(book));
        repo.add(book);
        logger.info("store new book " + book);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public boolean removeItemByParameter(final String itemParameterValue, String parameterName) {
        switch (parameterName) {
            case "id":
                repo.removeIf(book -> book.getId().equals(itemParameterValue));
                return true;
            case "title":
                repo.removeIf(book -> book.getTitle().equals(itemParameterValue));
                return true;
            case "author":
                repo.removeIf(book -> book.getAuthor().equals(itemParameterValue));
                return true;
            case "size":
                repo.removeIf(book -> book.getSize() == Integer.parseInt(itemParameterValue));
                return true;
        }
        return false;
    }
}
