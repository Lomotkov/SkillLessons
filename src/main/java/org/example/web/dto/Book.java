package org.example.web.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Book {
    private Integer id;
    @Size(max = 100)
    @NotNull
    @NotEmpty
    private String author;
    @Size(max = 100)
    @NotNull
    @NotEmpty
    private String title;
    @Digits(integer = 4, fraction = 0)
    @NotNull
    private Integer size;

    public Book(Integer id, String author, String title, Integer size) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.size = size;
    }

    public Book() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", size=" + size +
                '}';
    }
}
