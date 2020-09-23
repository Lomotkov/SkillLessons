package org.example.app.exeptions;

public class FileDownloadException extends Exception {

    final private String message;

    public FileDownloadException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
