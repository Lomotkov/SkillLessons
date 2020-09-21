package org.example.web.app.exeptions;

public class BookShelfLoginException extends Exception {

    final private String message;

    public BookShelfLoginException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
