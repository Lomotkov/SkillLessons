package org.example.web.app.services;

import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final ProjectRepository<Book> bookRepo;

    @Autowired
    public BookService(BookRepository<Book> bookRepo) {
        this.bookRepo = bookRepo;
    }

    public List<Book> getAllBooks() {
        return bookRepo.retrieveAll();
    }

    public boolean saveBook(Book book) {
        if (book.getAuthor().isEmpty()
                && book.getTitle().isEmpty()
                && book.getSize() == null) {
            return false;
        } else {
            bookRepo.store(book);
            return true;
        }
    }

    public boolean removeBookById(String bookIdToRemove) {
        return bookRepo.removeItemByParameter(bookIdToRemove, "id");
    }

    public boolean removeBooksByTitle(String title) {
       return bookRepo.removeItemByParameter(title, "title");
    }

    public boolean removeBooksByAuthor(String author) {
        return bookRepo.removeItemByParameter(author, "author");

    }

    public boolean removeBooksBySize(String size) {
        return bookRepo.removeItemByParameter(size, "size");

    }

    public List<Book> searchBooks(String searchParam) {
        return bookRepo.getAllItemsBySearchParam(searchParam);
    }
}
