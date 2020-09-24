package org.example.web.controllers;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.example.app.exeptions.BookShelfLoginException;
import org.example.app.exeptions.FileDownloadException;
import org.example.app.exeptions.FileUploadException;
import org.example.app.services.BookService;
import org.example.app.services.FileService;
import org.example.web.dto.Book;
import org.example.web.dto.BookIdToRemove;
import org.example.web.dto.FileName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;

@Controller
@RequestMapping(value = "/books")
@Scope("singleton")
public class BookShelfController {

    private Logger logger = Logger.getLogger(BookShelfController.class);
    private BookService bookService;
    private FileService fileService;

    @Autowired
    public BookShelfController(BookService bookService, FileService fileService) {
        this.bookService = bookService;
        this.fileService = fileService;
    }

    @GetMapping("/shelf")
    public String books(Model model) {
        logger.info("got book shelf");
        model.addAttribute("book", new Book());
        model.addAttribute("bookIdToRemove", new BookIdToRemove());
        model.addAttribute("booksList", bookService.getAllBooks());
        model.addAttribute("fileNamesList", fileService.getAllFilesNameFromServer());
        model.addAttribute("selectedFile", new FileName());
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
        fileService.uploadFileToServer(file);
        return "redirect:/books/shelf";
    }

    @GetMapping(value = "/downloadFile", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody
    byte[] downloadFile(@ModelAttribute("selectedFile") FileName fileName, HttpServletResponse response) throws Exception {
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName.getName());
        return fileService.downloadFileFromServer(fileName.getName());
    }

    @ExceptionHandler({FileDownloadException.class})
    public String handleErrorDownload(Model model, FileDownloadException exception) {
        model.addAttribute("errorMessage", exception.getMessage());
        return books(model);
    }

    @ExceptionHandler({FileUploadException.class})
    public String handleError(Model model, FileUploadException exception) {
        model.addAttribute("errorMessage", exception.getMessage());
        return books(model);
    }
}
