package org.example.app.exeptions;

public class FileUploadException extends Exception {

    final private String message;

    public FileUploadException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
