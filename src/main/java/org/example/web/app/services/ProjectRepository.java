package org.example.web.app.services;

import java.util.List;

public interface ProjectRepository<T> {
    List<T> retriveAll();

    void store(T book);

    boolean removeItemById(int bookIdToRemove);
}
