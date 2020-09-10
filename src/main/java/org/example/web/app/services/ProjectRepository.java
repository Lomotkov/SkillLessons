package org.example.web.app.services;

import java.util.List;

public interface ProjectRepository<T> {
    List<T> retrieveAll();

    void store(T book);

    boolean removeItemByParameter(String itemParameterValue, String parameterName);
}
