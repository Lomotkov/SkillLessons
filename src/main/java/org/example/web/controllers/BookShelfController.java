package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.app.exeptions.BookShelfLoginException;
import org.example.app.exeptions.FileUploadException;
import org.example.app.services.BookService;
import org.example.web.dto.Book;
import org.example.web.dto.BookIdToRemove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

@Controller
@RequestMapping(value = "/books")
@Scope("singleton")
public class BookShelfController {

    private Logger logger = Logger.getLogger(BookShelfController.class);
    private BookService bookService;

    @Autowired
    public BookShelfController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/shelf")
    public String books(Model model) {
        logger.info("got book shelf");
        model.addAttribute("book", new Book());
        model.addAttribute("bookIdToRemove", new BookIdToRemove());
        model.addAttribute("booksList", bookService.getAllBooks());
        return "book_shelf";
    }

    @PostMapping("/save")
    public String saveBook(@Valid Book book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", new Book());
            model.addAttribute("bookIdToRemove", new BookIdToRemove());
            model.addAttribute("booksList", bookService.getAllBooks());
            return "book_shelf";
        } else {
            bookService.saveBook(book);
            logger.info("currnet repository contents: " + bookService.getAllBooks().size());
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/remove/id")
    public String removeBook(@Valid BookIdToRemove bookIdToRemove, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", new Book());
            model.addAttribute("booksList", bookService.getAllBooks());
            return "book_shelf";
        } else {
            bookService.removeBookById(bookIdToRemove.getId());
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/remove/author")
    public String removeBooksByAuthor(@RequestParam(value = "bookAuthor") String bookAuthor) {
        if (bookService.removeBooksByAuthor(bookAuthor)) {
            logger.info("Deleted book with author = " + bookAuthor);
        }
        return "redirect:/books/shelf";
    }

    @PostMapping("/remove/title")
    public String removeBooksByTitle(@RequestParam(value = "bookTitle") String bookTitle) {
        if (bookService.removeBooksByTitle(bookTitle)) {
            logger.info("Deleted book with title = " + bookTitle);
        }
        return "redirect:/books/shelf";
    }

    @PostMapping("/remove/size")
    public String removeBookBySize(@RequestParam(value = "bookSize") String bookSize) {
        if (bookService.removeBooksBySize(bookSize)) {
            logger.info("Deleted book with Size = " + bookSize);
        }
        return "redirect:/books/shelf";
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        String name = file.getOriginalFilename();
        if(name.isEmpty()) {
            throw new FileUploadException("no file selected");
        }
        byte[] bytes = file.getBytes();
        //create dir
        String rootPath = System.getProperty("catalina.home");
        File dir = new File(rootPath + File.separator + "external_uploads");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        //create file
        File serverFile = new File(dir.getAbsolutePath() + File.separator + name);
        try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile))) {
            stream.write(bytes);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        logger.info("new file saved at: " + serverFile.getAbsolutePath());
        return "redirect:/books/shelf";
    }

    @ExceptionHandler(FileUploadException.class)
    public String handleError(Model model, FileUploadException exception) {
        model.addAttribute("errorMessage", exception.getMessage());
        model.addAttribute("book", new Book());
        model.addAttribute("bookIdToRemove", new BookIdToRemove());
        model.addAttribute("booksList", bookService.getAllBooks());
        return "book_shelf";
    }
}
